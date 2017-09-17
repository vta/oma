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
    
    init(inputs: [String]){
        
        run = inputs[0]
        block = inputs[1]
        coach = inputs[2]
        op = inputs[3]
        timeDue = inputs[4]
        firstTime = inputs[5]
        direction = inputs[6]
        lastTime = inputs[7]
        pullInTime = inputs[8]
        actual = inputs[9]
        ocp = inputs[10]
        oe = inputs[11]
        
        strings = [run, block, coach, op, timeDue, firstTime, direction, lastTime, pullInTime, actual, ocp, oe]
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
