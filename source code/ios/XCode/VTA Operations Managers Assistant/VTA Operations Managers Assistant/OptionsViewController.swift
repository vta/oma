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
    @IBOutlet weak var notificationField: UITextField!
    
    var oldPriority = 0
    var mainMenu: MainMenuViewController = MainMenuViewController()

    override func viewDidLoad() {
        super.viewDidLoad()
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
        
        request = NSFetchRequest<NSFetchRequestResult>(entityName: "PriorityPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                notificationField.text = result.value(forKey: "string") as? String
            }
        }
        catch {
            //ERROR
        }
        
        request = NSFetchRequest<NSFetchRequestResult>(entityName: "NotificationPreference")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                let p = result.value(forKey: "string") as? String
                var index = 0;
                if(p == "zz") {index = 0}
                if(p == "A") {index = 1}
                if(p == "C") {index = 2}
                if(p == "E") {index = 3}
                priorityMinimum.selectedSegmentIndex = index
                oldPriority = index
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
    
    @IBAction func notificationSubmit(_ sender: Any) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        //Fetch the core data and modify its value.
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "PriorityPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                result.setValue(notificationField.text, forKey: "string")
            } else {
                //No object yet exists in the core data; create one.
                let newPath = NSEntityDescription.insertNewObject(forEntityName: "PriorityPath", into: context)
                newPath.setValue(notificationField.text!, forKey: "string")
            }
            
            try context.save()
        }
        catch {
            //ERROR
        }
    }
    
    func prioritySubmit() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let context = appDelegate.persistentContainer.viewContext
        
        //Fetch the core data and modify its value.
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "NotificationPreference")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            var text = ""
            let index = priorityMinimum.selectedSegmentIndex
            if(index == 0) {text = "zz"}
            if(index == 1) {text = "A"}
            if(index == 2) {text = "C"}
            if(index == 3) {text = "E"}
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject

                result.setValue(text, forKey: "string")
            } else {
                //No object yet exists in the core data; create one.
                let newP = NSEntityDescription.insertNewObject(forEntityName: "NotificationPreference", into: context)
                newP.setValue(text, forKey: "string")
            }
            
            try context.save()
        }
        catch {
            //ERROR
        }
    }
    
    func getPriority(newPriority: Int) -> String{
        var column: String = "zz"
        if(newPriority == 1) {
            column = "A"
        } else {
            if(newPriority == 2) {
                column = "C"
            } else {
                if(newPriority == 3) {
                    column = "E"
                }
            }
        }
        return column
    }
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
            // Get the new view controller using segue.destinationViewController.
            // Pass the selected object to the new view controller.
        
        prioritySubmit()
            
        if segue.destination is MainMenuViewController{
            let menu = (segue.destination as! MainMenuViewController)

            let newPriority = priorityMinimum.selectedSegmentIndex
            if(newPriority != oldPriority) {
                let column = getPriority(newPriority: newPriority)
                menu.newPriority = newPriority
                menu.priorityColumn = column
                menu.oldPriority = oldPriority
            }
        }
    }
}
