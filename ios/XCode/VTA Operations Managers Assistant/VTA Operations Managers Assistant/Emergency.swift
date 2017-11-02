//
//  Emergency.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 12/30/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit

class Emergency {
    
    // MARK: Properties
    
    var priority: String
    var descriptor: String
    var time: String
    var date: String
    var location: String
    var direction: String
    var vehicle1: String
    var vehicle2: String
    var block: String
    var badge1: String
    var badge2: String
    var name1: String
    var name2: String
    var respondingAgencies: String
    var mediaOnScene: String
    var respondingSupervisors: String
    var injuries: String
    var postAccidentTestRequired: String
    var description: String
    
    var strings: [String]
    
    static var labels = ["Level of Priority", "Descriptor", "Time of Day", "Location", "Direction", "Vehicle 1", "Vehicle 2", "Block", "Badge 1", "Badge 2", "Name 1", "Name 2", "Responding Agencies", "Media on Scene", "Responding Supervisors", "Injuries", "Post-Accident Test Required", "Description of Event"]
    
    // MARK: Initialization
    
    init(words: [String]){
        
        var inputs = [String]()
        
        for i in 0...words.count - 1{
            inputs.append(words[i].replacingOccurrences(of: "ß", with: ","))
        }
        
        priority = inputs[0]
        descriptor = inputs[1]
        time = inputs[2]
        date = inputs[3]
        location = inputs[4]
        direction = inputs[5]
        vehicle1 = inputs[6]
        vehicle2 = inputs[7]
        block = inputs[8]
        badge1 = inputs[9]
        badge2 = inputs[10]
        name1 = inputs[11]
        name2 = inputs[12]
        respondingAgencies = inputs[13]
        mediaOnScene = inputs[14]
        respondingSupervisors = inputs[15]
        injuries = inputs[16]
        postAccidentTestRequired = inputs[17]
        description = inputs[18]
        
        strings = [priority, descriptor, time + " on " + date, location, direction, vehicle1, vehicle2, block, badge1, badge2, name1, name2, respondingAgencies, mediaOnScene, respondingSupervisors, injuries, postAccidentTestRequired, description]
    }
    
    //default values
    init() {
        priority = "priority"
        descriptor = "descriptor"
        time = "time"
        date = "date"
        location = "location"
        direction = "direction"
        vehicle1 = "vehicle 1"
        vehicle2 = "vehicle 2"
        block = "block"
        badge1 = "badge 1"
        badge2 = "badge 2"
        name1 = "name 1"
        name2 = "name 2"
        respondingAgencies = "responding agencies"
        mediaOnScene = "media on scene"
        respondingSupervisors = "responding supervisors"
        injuries = "injuries"
        postAccidentTestRequired = "post-accident test required"
        description = "description"
        strings = [priority, descriptor, time + " on " + date, location, direction, vehicle1, vehicle2, block, badge1, badge2, name1, name2, respondingAgencies, mediaOnScene, respondingSupervisors, injuries, postAccidentTestRequired, description]
    }
    
}
