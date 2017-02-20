//
//  SearchBookCell.swift
//  Dodo
//
//  Created by Kevin Ling on 7/18/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import Cosmos

class SearchBookCell: UITableViewCell {

    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    @IBOutlet weak var coverView: UIImageView!

    @IBOutlet weak var rateNumberView: UILabel!
    @IBOutlet weak var rateView: CosmosView!
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var authorView: UILabel!
    @IBOutlet weak var categoryView: UILabel!
}

class SearchTrackCell: UITableViewCell {
    @IBOutlet weak var forkView: UILabel!
    
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var starView: UILabel!
    @IBOutlet weak var trackImgView: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.trackImgView.layer.cornerRadius = 30
    }
    
}
