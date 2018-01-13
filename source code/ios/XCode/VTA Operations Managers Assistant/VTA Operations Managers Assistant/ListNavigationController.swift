//
//  EmergencyListNavigationController.swift
//  VTA Operations Managers Assistant
//
//  Created by Nathan Walker on 10/25/17.
//  Copyright © 2017 Nathan Walker. All rights reserved.
//

import UIKit
import GoogleAPIClientForREST
import GoogleSignIn

class ListNavigationController: UINavigationController {
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    func setRows(rows: [Any]) {
        print(rows)
        for c in self.childViewControllers {
            if c is EmergencyListTableViewController {
                (c as! EmergencyListTableViewController).rows = rows as! [[String]]
                (c as! EmergencyListTableViewController).loadEmergencies()
                (c as! EmergencyListTableViewController).tableView.reloadData()
            }
            if c is OCPListTableViewController {
                (c as! OCPListTableViewController).rows = rows as! [[String]]
                (c as! OCPListTableViewController).loadOCPs()
                (c as! OCPListTableViewController).tableView.reloadData()
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
