//
//  MeTableViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import SwiftyJSON

class MeTableViewController: UITableViewController {
    var provider: Networking!
    
    struct StoryBoard {
        static let logout = "Log Out"
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        self.provider = Networking.newDefaultNetworking()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        let appToken = XAppToken()
        name.text = appToken.firstname! + " " + appToken.lastname!
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBOutlet weak var name: UILabel!
    @IBAction func logout(sender: UIButton) {
        var token = XAppToken()
        if token.loginState == true {
            self.provider.request(DodoApi.Logout)
                .subscribe { (event) -> Void in
                    switch event {
                    case .Next(let response):
                        print(response)
                        let json = JSON(data: response.data)
                        if let status = json["status"].int {
                            switch status {
                            default:
                                break
                            }
                        }
                    case .Error(let error):
                        print("error. \(error)")
                    default:
                        break
                    }
                }
        }
        token.loginState = false
        performSegueWithIdentifier(StoryBoard.logout, sender: nil)
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if indexPath.section == 1 && indexPath.row == 0 {
            // achievement
        }
    }

}
