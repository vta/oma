//
//  OCP
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 12/30/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit

class OCP {
    
    // MARK: Properties
    
    var run: String
    var block: String
    var coach: String
    var op: String
    var timeDue: String
    var firstTime: String
    var direction: String
    var lastTime: String
    var pullInTime: String
    var actual: String
    var ocp: String
    var oe: String
    
    var strings: [String]
    
    static var labels = ["Run", "Block", "Coach", "Operator", "Time Due", "First Time", "Direction",
                                "Last Time", "Pull-In Time", "Actual", "O/C/P", "Op/Eq"]
    
    // MARK: Initialization
    
    init(words: [String]){
        strings = words;
        
        while(strings.count < 12) {
            strings.append("N/A")
        }
        
        run = words[0]
        block = words[1]
        coach = words[2]
        op = words[3]
        timeDue = words[4]
        firstTime = words[5]
        direction = words[6]
        lastTime = words[7]
        pullInTime = words[8]
        actual = words[9]
        ocp = words[10]
        oe = words[11]
    }
    
    init(){
        run = "Run"
        block = "Block"
        coach = "Coach"
        op = "Operator"
        timeDue = "Time Due"
        firstTime = "First Time"
        direction = "Direction"
        lastTime = "Last Time"
        pullInTime = "Pull-In Time"
        actual = "Actual"
        ocp = "Outlate/Cancel/Pull"
        oe = "Operator/Equipment"
        strings = OCP.labels
    }
    
}
