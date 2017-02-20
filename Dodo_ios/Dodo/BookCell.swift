//
//  TrackCell.swift
//  Dodo
//
//  Created by Kevin Ling on 7/10/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit


@IBDesignable
class BookCell: UITableViewCell {

    @IBOutlet weak var pagesReadView: UILabel!
    
    @IBOutlet weak var totalPagesView: UILabel!
    
    @IBOutlet weak var percentageView: UILabel!
    @IBOutlet weak var circularProgressView: KDCircularProgress!
    @IBOutlet weak var titleView: UILabel!
    @IBOutlet weak var typeView: UIImageView!
    @IBOutlet weak var lastUpdateView: UILabel!
    @IBOutlet weak var currentStreak: UILabel!
}

