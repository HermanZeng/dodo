//
//  DebugViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/5/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class DebugViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var restUrl: UITextField! {
        didSet {
            restUrl.delegate = self
        }
    }
    
    override func viewDidLoad() {
        
        // load default url set last time.
        if let url = NSUserDefaults.standardUserDefaults().stringForKey(Constants.RestApi.BaseURLKey) {
            Constants.RestApi.BaseURL = url
        }
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        if let url = textField.text {
            if url.isNotEmpty {
                Constants.RestApi.BaseURL = url
                NSUserDefaults.standardUserDefaults().setObject(url, forKey: Constants.RestApi.BaseURLKey)
                print(Constants.RestApi.BaseURL)
            }
        }
        return true
    }
}
