//
//  MessageCell.swift
//  Dodo
//
//  Created by Kevin Ling on 9/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class MessageCell: UITableViewCell {

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        self.messageView.font?.fontWithSize(20)
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @IBOutlet weak var type: UILabel!
    @IBOutlet weak var messageView: UITextView!
}
