//
//  BottomShadowView.swift
//  Dodo
//
//  Created by Kevin Ling on 7/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

@IBDesignable
class BottomShadowView: UIView {

    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    
    override func awakeFromNib() {
        super.awakeFromNib()
        self.layer.shadowColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.07).CGColor
        self.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        self.layer.shadowOpacity = 1.0
        self.layer.shadowRadius = 0.0
        
        self.layer.cornerRadius = 5
    }

}
