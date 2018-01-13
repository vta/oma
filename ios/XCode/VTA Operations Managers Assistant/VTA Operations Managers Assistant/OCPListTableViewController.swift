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
    var rows: [[String]] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadOCPs()
    }
    
    
    func loadOCPs() {
        for row in rows {
            var arr: [String] = []
            for s in row {
                arr.append(s)
            }
            print(arr)
            ocps.append(OCP.init(words: arr))
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
            (segue.destination as! OCPNavigationController).ocp = ocps[tableView.indexPathForSelectedRow!.row]
            (segue.destination as! OCPNavigationController).ocps = ocps}
        // Pass the selected object to the new view controller.
    }
    
}
