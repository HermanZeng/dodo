//
//  BookShadowView.swift
//  Dodo
//
//  Created by Kevin Ling on 7/14/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class BookShadowView: UIView {

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
        
        self.clipsToBounds = false
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        let size = self.bounds.size;
        let curlFactor = 15.0 as CGFloat;
        let shadowDepth = 5.0 as CGFloat;
        var path = UIBezierPath()
        path.moveToPoint(CGPointMake(0, 0))
        path.addLineToPoint(CGPointMake(size.width, 0))
        path.addLineToPoint(CGPointMake(size.width, size.height + shadowDepth))
        path.addCurveToPoint(CGPointMake(0, size.height + shadowDepth), controlPoint1: CGPointMake(size.width - curlFactor, size.height + shadowDepth - curlFactor), controlPoint2: CGPointMake(curlFactor, size.height + shadowDepth - curlFactor))
        
        self.layer.shadowPath = path.CGPath;
        self.layer.shadowRadius = 4
        self.layer.shadowOffset = CGSizeMake(0, 5)
        self.layer.shadowColor = UIColor.blackColor().CGColor
        self.layer.shadowOpacity = 0.5
    }
}
