//
//  OCPListTableViewController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 8/9/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit
import CoreData

class OCPListTableViewController: UITableViewController {
    // MARK: Properties
    var ocps = [OCP]()
    var filePath = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        fetchFilePath()
        loadOCPs()
    }
    
    func fetchFilePath() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "DataPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                let filePath = result.value(forKey: "string") as! String
                print("path: " + filePath)
            }
        }
        catch {
            //ERROR
        }
    }
    
    func loadOCPs() {
        var text = ""
        do{
            if let url = URL.init(string: filePath) {
                try text = String(contentsOf: url, encoding: String.Encoding.utf8)
            } else {
                text = "715(1),7942,1344,11832,2:13PM,7:23AM,Westbound,7:38PM,7:57PM,N/A,Pull,Operator\n835(1),5442,1044,11832,3:13AM,9:23AM,Northbound,7:38AM,7:57PM,N/A,Pull,Equipment\n715(1),5442,1044,11832,9:37AM,9:23AM,Eastbound,7:38PM,7:57PM,8:57PM,Outlate,Equipment\n715(1),5342,1044,11832,10:13AM,8:23AM,Northbound,6:38PM,7:57PM,N/A,Cancel,Operator\n715(1),6442,1044,11832,6:13AM,6:23AM,Southbound,7:38PM,7:57PM,N/A,Pull,Operator\n770(1),5442,1044,11832,11:13AM,8:23AM,Northbound,7:38PM,7:57PM,8:57PM,Outlate,Operator\n715(1),5442,1044,11832,6:13AM,6:23AM,Eastbound,12:38PM,7:57PM,N/A,Cancel,Equipment\n715(1),5442,1454,11832,6:37AM,4:23AM,Westbound,7:38PM,7:57PM,N/A,Cancel,Operator\n715(1),5532,6744,11832,2:13AM,6:23AM,Westbound,7:38PM,7:57PM,8:57PM,Outlate,Equipment\n715(1),5442,2244,11832,2:13AM,6:23AM,Northbound,7:38PM,7:57PM,N/A,Cancel,Operator\n715(1),7942,1684,11832,6:13AM,3:23AM,Northbound,7:38PM,7:57PM,N/A,Pull,Operator"
            }
        } catch {
            print("ERROR: File not found.")
            showAlert()
            text = "<Run>, <Block>, <Coach>, <Operator>, <Time Due>, <First Time>, <Direction>, <Last Time>, <Pull-In Time>, <Actual>, <O/C/P>, <Op/Eq>"
        }
        
        let strList = text.components(separatedBy: "\n").dropFirst()
        
        for s in strList {
            let values = s.components(separatedBy: ",")
            if values.count == 12 {
                ocps.append(OCP(inputs: values))
            }
        }
    }
    
    func showAlert() {
        let alert = UIAlertController(title: "Missing File", message: "Invalid file path. Please change the path in Options.", preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
        //                alert.addAction(UIAlertAction(title: "Options", style: .default) { (action) -> Void in self.goToOptions()})
        present(alert, animated: true, completion: nil);
    }
    
    //    func goToOptions() {
    //        let options = self.storyboard?.instantiateViewController(withIdentifier: "OptionsViewController") as! OptionsViewController
    //        self.navigationController?.pushViewController(OptionsViewController, animated: true)
    //    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: - Table view data source
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return ocps.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "OCPListContentCell"
        
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as! OCPListContentCell
        
        // Fetches the appropriate meal for the data source layout.
        let ocp = ocps[indexPath.row]
        
        cell.ocpLabel.text = ocp.ocp
        cell.timeDueLabel.text = ocp.timeDue
        
        return cell
    }
    
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        if segue.destination is OCPNavigationController {
            (segue.destination as! OCPNavigationController).ocp = ocps[tableView.indexPathForSelectedRow!.row]}
        // Pass the selected object to the new view controller.
    }
    
}
