//
//  OptionsViewController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 7/26/17.
//  Copyright © 2017 Nathan Walker. All rights reserved.
//

import UIKit
import CoreData
import GoogleAPIClientForREST
import GoogleSignIn

class OptionsViewController: UIViewController {
    

    //MARK: Outlets
    @IBOutlet weak var priorityMinimum: UISegmentedControl!
    @IBOutlet weak var emergencyField: UITextField!
    @IBOutlet weak var ocpField: UITextField!
    
    var initialPriority = 0

    
    override func viewDidLoad() {
        super.viewDidLoad()
        initialPriority = priorityMinimum.selectedSegmentIndex
        setTextFields()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setTextFields() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        var request = NSFetchRequest<NSFetchRequestResult>(entityName: "EmergencyPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)

            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                emergencyField.text = result.value(forKey: "string") as? String
            }
        }
        catch {
            //ERROR
        }
        
        request = NSFetchRequest<NSFetchRequestResult>(entityName: "OCPPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                ocpField.text = result.value(forKey: "string") as? String
            }
        }
        catch {
            //ERROR
        }
    }

    // MARK: Actions
    @IBAction func EmergencySubmit(_ sender: Any) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        //Fetch the Emergency core data and modify its value.
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "EmergencyPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                result.setValue(emergencyField.text, forKey: "string")
            } else {
                //No object yet exists in the core data; create one.
                let newPath = NSEntityDescription.insertNewObject(forEntityName: "EmergencyPath", into: context)
                newPath.setValue(emergencyField.text!, forKey: "string")
            }
            
            try context.save()
        }
        catch {
            //ERROR
        }
    }
    
    @IBAction func ocpSubmit(_ sender: Any) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        //Fetch the Emergency core data and modify its value.
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "OCPPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                result.setValue(ocpField.text, forKey: "string")
            } else {
                //No object yet exists in the core data; create one.
                let newPath = NSEntityDescription.insertNewObject(forEntityName: "OCPPath", into: context)
                newPath.setValue(ocpField.text!, forKey: "string")
            }
            
            try context.save()
        }
        catch {
            //ERROR
        }
    }
    
    
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        if(priorityMinimum.selectedSegmentIndex != initialPriority) {
            var token = ""
            
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            let context = appDelegate.persistentContainer.viewContext
            
            let request = NSFetchRequest<NSFetchRequestResult>(entityName: "DeviceToken")
            request.returnsObjectsAsFaults = false
            
            do {
                let results = try context.fetch(request)
                
                if results.count >= 1 {
                    let result = results[0] as! NSManagedObject
                    token = result.value(forKey: "string") as! String
                }
            }
            catch {
                //ERROR
            }
            
            let newPriority = priorityMinimum.selectedSegmentIndex
            addToPriorityList(token: token, newPriority: newPriority, oldPriority: initialPriority)
        }
    }
    
    func addToPriorityList(token: String, newPriority: Int, oldPriority: Int) {
        var column = "B"
        if(newPriority == 1) {column = "B"}
        if(newPriority == 2) {column = "C"}
        if(newPriority == 3) {column = "D"}
        
        let service = GTLRService()
        let spreadsheetId = "1PN4USOBuPylNzyJ3xxNvw5uEUxoPzSr"
        let range = "Sheet1!" + column + ""
        let valueRange = GTLRSheets_ValueRange.init();
        valueRange.values = [
            [token]
        ]
        let query = GTLRSheetsQuery_SpreadsheetsValuesAppend
            .query(withObject: valueRange, spreadsheetId:spreadsheetId, range:range)
        query.valueInputOption = "USER_ENTERED"
        service.executeQuery(query,
                             delegate: self,
                             didFinish: Selector(("displayResultWithTicket:finishedWithObject:error:"))
        )
        
    }
 

}
