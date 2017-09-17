//
//  OptionsViewController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 7/26/17.
//  Copyright © 2017 Nathan Walker. All rights reserved.
//

import UIKit
import CoreData

class OptionsViewController: UIViewController {

    //MARK: Outlets
    @IBOutlet weak var priorityMinimum: UISegmentedControl!
    @IBOutlet weak var dataPathField: UITextField!
 
    
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
        
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "DataPath")
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)

            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                dataPathField.text = result.value(forKey: "string") as? String
                print("set to " + (result.value(forKey: "string") as! String))
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
        let request = NSFetchRequest<NSFetchRequestResult>(entityName: "DataPath")
        
        request.returnsObjectsAsFaults = false
        
        do {
            let results = try context.fetch(request)
            
            if results.count >= 1 {
                let result = results[0] as! NSManagedObject
                result.setValue(dataPathField.text, forKey: "string")
            } else {
                //No object yet exists in the core data; create one.
                let newPath = NSEntityDescription.insertNewObject(forEntityName: "DataPath", into: context)
                newPath.setValue(dataPathField.text!, forKey: "string")
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
    }
 

}
