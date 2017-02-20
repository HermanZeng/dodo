//
//  Constant.swift
//  Dodo
//
//  Created by Kevin Ling on 7/4/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import UIKit

struct Constants {
    struct UserDefault {
        static let LoginState = "Login State"
    }
    struct RestApi {
        static var BaseURLKey = "url"
        //static var BaseURL = "http://182.254.135.243:8000"
        static var BaseURL = "http://192.168.1.7:8000"
    }
    struct Error {
        static let InvalidEmail = "The email is not valid"
        static let InvalidPassword = "Your password is too short"
        static let InvalidName = "Name shouldn't be blank"
        static let WrongPassword = "wrong password"
        static let WrongEmail = "account not found"
        static let ExistingEmail = "Account already exists. Consider Sign in"
    }
    
    struct Color {
        static let DefaultBlue = UIColor(red: 0, green: 128.0/255, blue: 1, alpha: 1.0)
        static let DisableGray = UIColor.lightGrayColor()
    }
    
    static let category:[String:Int] = ["文学":1, "流行":2, "文化":3, "生活":4, "经管":5, "科技":6, "学习":7]
    static let categoryDict:[Int: String] = [1:"文学", 2:"流行", 3:"文化", 4:"生活", 5:"经管", 6:"科技", 7:"学习"]
    
    static let messageDict:[Int: String] = [1: "Honor", 2:"Pull Request", 3:"Star", 4:"Long Comment", 5:"Short Comment", 6:"Track Comment", 7:"Track Review"]

}
