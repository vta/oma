//
//  EmergencyListContentCellTableViewCell.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 12/31/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit

class EmergencyListContentCell: UITableViewCell {

    //MARK: Properties
    @IBOutlet weak var priorityLabel: UILabel!
    @IBOutlet weak var dateTimeLabel: UILabel!
    @IBOutlet weak var locationLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
