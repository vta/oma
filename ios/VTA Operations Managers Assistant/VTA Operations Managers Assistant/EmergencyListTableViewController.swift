//
//  EmergencyListTableViewController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 8/9/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit
import GoogleAPIClientForREST
import GoogleSignIn

class EmergencyListTableViewController: UITableViewController {
    // MARK: Properties
    var emergencies = [Emergency]()
    var filePath: String = ""
    var rows: [[String]] = []
    
    let signInButton = GIDSignInButton()
    private let service = GTLRSheetsService()
    private let scopes = [kGTLRAuthScopeSheetsSpreadsheetsReadonly]

    
    
    override func viewDidLoad() {
        loadEmergencies()
    }
    
    func loadEmergencies() {
        for row in rows {
            var arr: [String] = []
            for s in row {
                arr.append(s)
            }
            print(arr)
            emergencies.append(Emergency.init(words: arr))
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
            (segue.destination as! EmergencyNavigationController).emergencies = emergencies
        }
        // Pass the selected object to the new view controller.
    }    
}
