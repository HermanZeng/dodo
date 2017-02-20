//
//  RoundButton.swift
//  Dodo
//
//  Created by Kevin Ling on 7/21/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

@IBDesignable
class RoundButton: UIButton {
    
    @IBInspectable
    var cornerRadius: CGFloat = 0.0
    
    var disabled: Bool = true
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        self.layer.cornerRadius = cornerRadius
        if disabled == false {
            self.layer.borderColor = Constants.Color.DefaultBlue.CGColor
            self.tintColor = Constants.Color.DefaultBlue
        } else {
            self.layer.borderColor = Constants.Color.DisableGray.CGColor
            self.tintColor = Constants.Color.DisableGray
        }
    }
    
    func setDisabled() {
        self.layer.borderColor = Constants.Color.DisableGray.CGColor
        self.tintColor = Constants.Color.DisableGray
    }
    
    func setActive() {
        self.layer.borderColor = Constants.Color.DefaultBlue.CGColor
        self.tintColor = Constants.Color.DefaultBlue
    }
}
