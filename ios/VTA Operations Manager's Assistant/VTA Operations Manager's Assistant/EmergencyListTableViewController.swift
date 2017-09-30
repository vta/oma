//
//  EmergencyListTableViewController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 8/9/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit
import CoreData

class EmergencyListTableViewController: UITableViewController{
    // MARK: Properties
    var emergencies = [Emergency]()
    var filePath: String = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        //fetchFilePath()
        loadEmergenciesLocally()
        loadEmergenciesSMB()
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
    
    func loadEmergenciesSMB() {
        
        //
        //
        //        var prefix = String()
        //        var fileName = String()
        //        var data: NSMutableData = NSMutableData()
        //
        //        let session = URLSession(configuration: URLSessionConfiguration.default)
        //        let task = session.dataTask(with: URL.init(string: prefix + fileName)!)
        //        task.resume()
        //
        //        let session = URLSession(configuration: URLSessionConfiguration.default)
        //        if let url = URL(string: "smb://192.168.1.130:TestFile.csv") {
        //            (session.dataTask(with: url) { (data, response, error) in
        //                if let error = error {
        //                    print("Error: \(error)")
        //                } else if let response = response,
        //                    let data = data,
        //                    let string = String(data: data, encoding: .utf8) {
        //                    print("Response: \(response)")
        //                    print("DATA:\n\(string)\nEND DATA\n")
        //                }
        //            }).resume()
        //        }
        
        //        // variable to contain the read method returning value
        //        var fileContent: String = ""
        //
        //        // init the SMBDriver
        //        let smbDriver: SMBDriver = SMBDriver()
        //
        //        // set debug mode to true
        //        smbDriver.debug = true
        //
        //        // set the connect data you would like to use while reading
        //        let hostName: String = "192.168.1.130"
        //        let userName: String = "test"
        //        let loginPassword: String = "1234"
        //        let fileNameAndPath: String = "/TestFile"
        //        let sharedFolder: String = "MyShare"
        //
        //        do
        //        {
        //            // read a string from a text file on SMB share, if the file does not exists (and the user does not have read permissions) an error will be thrown
        //            fileContent = try smbDriver.readTextFile(fromHost: hostName, withLogin: userName, withPassword: loginPassword, withFileName: fileNameAndPath, onShare: sharedFolder)
        //            NSLog("Successfully read file, here is its content:\n\(fileContent)")
        //        }
        //        catch
        //        {
        //            NSLog("failed to read file content, errorCode: \((error as NSError).code), errorMessage: \((error as NSError).localizedDescription)");
        //        }
        
    }
    
    //Function for obtaining data without using SMB. For testing purposes only.
    func loadEmergenciesLocally() {
        var text = ""
        do{
            if let url = URL.init(string: filePath) {
                try text = String(contentsOf: url, encoding: String.Encoding.utf8)
            } else {
                text = "Low,Early,12:30 AM,January 22,5 First Street,Northbound,G7JDE2,H4WEQ5,2456,9384720,9382716,George Harris,Sarah Michaels,None,None,None,Broken Leg,No,Nothing major.\nMedium,Early,4:15 PM,January 21,800 Pennsylvania Ave,Northbound,G7JDE2,H4WEQ5,2456,9384720,9382716,George Harris,Sarah Michaels,None,None,None,Broken Leg,No,Nothing major.\nHigh,Early,7:45 AM,January 21,5 First Street,Northbound,G7JDE2,H4WEQ5,2456,9384720,9382716,George Harris,Sarah Michaels,None,None,None,Broken Leg,No,Nothing major."
            }
        } catch {
            print("ERROR: File not found.")
            showAlert()
            text = "<Priority>, <Descriptor>, <Time>, <Date>, <Location>, <Direction>, <Vehicle #1>, <Vehicle #2>, <Block>, <Badge #1>, <Badge #2>, <Name #1>, <Name #2>, <Responding Agencies>, <Media on Scene>, <Responding Supervisors>, <Injuries>, <Post-Accident Test Required>, <description>"
        }
        
        let strList = text.components(separatedBy: "\n").dropFirst()
        
        for s in strList {
            let values = s.components(separatedBy: ",")
            if values.count == 19 {
                emergencies.append(Emergency(words: values))
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
        return emergencies.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "EmergencyListContentCell"
        
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as! EmergencyListContentCell
        
        // Fetches the appropriate meal for the data source layout.
        let emergency = emergencies[indexPath.row]
        
        let priority = emergency.priority
        cell.priorityLabel.text = priority + " Priority"
        if priority == "Low" {cell.priorityLabel.textColor = UIColor.green}
        if priority == "Medium" {cell.priorityLabel.textColor = UIColor.orange}
        if priority == "High" {cell.priorityLabel.textColor = UIColor.red}
        cell.dateTimeLabel.text = (emergency.time + " on " + emergency.date)
        cell.locationLabel.text = emergency.location
        
        return cell
    }
    
    /*
     // Override to support conditional editing of the table view.
     override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
     // Return false if you do not want the specified item to be editable.
     return true
     }
     */
    
    /*
     // Override to support editing the table view.
     override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
     if editingStyle == .Delete {
     // Delete the row from the data source
     tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
     } else if editingStyle == .Insert {
     // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
     }
     }
     */
    
    /*
     // Override to support rearranging the table view.
     override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {
     
     }
     */
    
    /*
     // Override to support conditional rearranging of the table view.
     override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
     // Return false if you do not want the item to be re-orderable.
     return true
     }
     */
    
    
    // MARK: - Navigation
    
    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        if segue.destination is EmergencyNavigationController {
            (segue.destination as! EmergencyNavigationController).emergency = emergencies[tableView.indexPathForSelectedRow!.row]
        }
        // Pass the selected object to the new view controller.
    }    
}