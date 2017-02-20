//
//  BookCell.swift
//  Dodo
//
//  Created by Kevin Ling on 7/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class TrackCell: UITableViewCell {

    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.layer.cornerRadius = 2
        self.layer.masksToBounds = true
    }

    @IBOutlet weak var upperBarView: UIView!

    @IBOutlet weak var numOfBooksView: UILabel!
    @IBOutlet weak var typeView: UILabel!
    @IBOutlet weak var nameView: UILabel!
    @IBOutlet weak var alreadyRead: UILabel!
    @IBOutlet weak var progressCircle: KDCircularProgress!
    @IBOutlet weak var progress: UILabel!
    @IBOutlet weak var currentStage: UILabel!
}
