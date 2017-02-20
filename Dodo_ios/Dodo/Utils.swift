//
//  Utils.swift
//  Dodo
//
//  Created by Kevin Ling on 7/4/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import UIKit
import ChameleonFramework

func isValidEmail(testStr:String) -> Bool {
    // print("validate calendar: \(testStr)")
    let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
    
    let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
    return emailTest.evaluateWithObject(testStr)
}

func isValidPassword(testStr:String) -> Bool {
    // simple check
    return testStr.characters.count > 4
}

class NavController: UINavigationController {
    
    override func viewDidLoad() {
        self.navigationBar.tintColor = ContrastColorOf(FlatWhite(), returnFlat: true)
    }
}

func NetworkSucks(view: UIView) {
    view.dodo.error("Please check your network connection!")
}

