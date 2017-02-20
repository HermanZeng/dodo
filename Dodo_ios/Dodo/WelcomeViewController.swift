//
//  WelcomeViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/4/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit

class WelcomeViewController: UIViewController {

    struct StoryBoard {
        static let login = "Need Login"
        static let main = "To Main"
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
    }
    
    override func viewDidAppear(animated: Bool) {
        super.viewDidAppear(animated)
        
        let defaults = NSUserDefaults.standardUserDefaults()
        var isLogin = defaults.boolForKey(Constants.UserDefault.LoginState)
        
        // DEBUG:
        //isLogin = true
        
        if !isLogin {
            performSegueWithIdentifier(StoryBoard.login, sender: nil)
        } else {
            performSegueWithIdentifier(StoryBoard.main, sender: nil)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        let tok = XAppToken()
        if let nvc = segue.destinationViewController as? LoginViewController {
            nvc.userEmail = tok.email
            nvc.userPwd = tok.password
        }
    }
    
    override func supportedInterfaceOrientations() -> UIInterfaceOrientationMask {
        return UIInterfaceOrientationMask.Portrait
    }
    

}
