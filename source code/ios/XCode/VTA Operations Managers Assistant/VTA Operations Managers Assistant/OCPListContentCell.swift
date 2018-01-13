//
//  OCPListContentCellTableViewCell.swift
//  VTA Emergency Viewer
//
//  Created by Nathan Walker on 12/31/16.
//  Copyright © 2016 Nathan Walker. All rights reserved.
//

import UIKit

class OCPListContentCell: UITableViewCell {

    //MARK: Properties
    @IBOutlet weak var ocpLabel: UILabel!
    @IBOutlet weak var timeDueLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
