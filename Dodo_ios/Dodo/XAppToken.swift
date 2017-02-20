//
//  XAuthToken.swift
//  Dodo
//
//  Created by Kevin Ling on 7/5/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation

private extension NSDate {
    var isInPast: Bool {
        let now = NSDate()
        return self.compare(now) == NSComparisonResult.OrderedAscending
    }
}

struct XAppToken {
    enum DefaultsKeys: String {
        case TokenKey = "TokenKey"
        case TokenExpiry = "TokenExpiry"
        case TokenUserEmail = "TokenUserEmail"
        case TokenUserPassword = "TokenUserPassword"
        case TokenLogin = "TokenLogin"
        case TokenUserFirstname = "TokenUserFirstname"
        case TokenUserLastname = "TokenUserLastname"
    }
    
    // MARK: - Initializers
    
    let defaults: NSUserDefaults
    
    init(defaults:NSUserDefaults) {
        self.defaults = defaults
    }
    
    init() {
        self.defaults = NSUserDefaults.standardUserDefaults()
    }
    
    
    // MARK: - Properties
    
    var token: String? {
        get {
            let key = defaults.stringForKey(DefaultsKeys.TokenKey.rawValue)
            return key
        }
        set(newToken) {
            defaults.setObject(newToken, forKey: DefaultsKeys.TokenKey.rawValue)
        }
    }
    
    var expiry: NSDate? {
        get {
            return defaults.objectForKey(DefaultsKeys.TokenExpiry.rawValue) as? NSDate
        }
        set(newExpiry) {
            defaults.setObject(newExpiry, forKey: DefaultsKeys.TokenExpiry.rawValue)
        }
    }
    
    var expired: Bool {
        if let expiry = expiry {
            return expiry.isInPast
        }
        return true
    }
    
    var isValid: Bool {
        if let token = token {
            return token.isNotEmpty && !expired
        }
        
        return false
    }
    
    var email: String? {
        get {
            let email = defaults.stringForKey(DefaultsKeys.TokenUserEmail.rawValue)
            return email
        }
        set(newEmail) {
            defaults.setObject(newEmail, forKey: DefaultsKeys.TokenUserEmail.rawValue)
        }
    }
    
    var password: String? {
        get {
            let pwd = defaults.stringForKey(DefaultsKeys.TokenUserPassword.rawValue)
            return pwd
        }
        set(newPwd) {
            defaults.setObject(newPwd, forKey: DefaultsKeys.TokenUserPassword.rawValue)
        }
    }
    
    var firstname: String? {
        get {
            let pwd = defaults.stringForKey(DefaultsKeys.TokenUserFirstname.rawValue)
            return pwd
        }
        set(newPwd) {
            defaults.setObject(newPwd, forKey: DefaultsKeys.TokenUserFirstname.rawValue)
        }
    }
    
    var lastname: String? {
        get {
            let pwd = defaults.stringForKey(DefaultsKeys.TokenUserLastname.rawValue)
            return pwd
        }
        set(newPwd) {
            defaults.setObject(newPwd, forKey: DefaultsKeys.TokenUserLastname.rawValue)
        }
    }
    
    var loginState: Bool? {
        get {
            let login = defaults.boolForKey(DefaultsKeys.TokenLogin.rawValue)
            return login
        }
        set(state) {
            defaults.setObject(state, forKey: DefaultsKeys.TokenLogin.rawValue)
        }
    }
}
