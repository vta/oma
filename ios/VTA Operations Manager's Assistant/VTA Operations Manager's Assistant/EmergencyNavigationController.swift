//
//  EmergencyNavigationController.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 5/2/17.
//  Copyright © 2017 Nathan Walker. All rights reserved.
//

import UIKit

class EmergencyNavigationController: UINavigationController {
    
    var emergency = Emergency()
    override func viewDidLoad() {
        super.viewDidLoad()
        print(emergency.date)
        (self.childViewControllers[0] as! EmergencyTableViewController).emergency = emergency

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.'
    }

}
