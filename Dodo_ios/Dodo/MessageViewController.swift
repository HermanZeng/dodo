//
//  MessageViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 9/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import ChameleonFramework

class MessageViewController: UITableViewController {

    var messages: [Message]?
    var provider: Networking = Networking.newDefaultNetworking()
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        let nib = UINib(nibName: "MessageCell", bundle: nil)
        self.tableView.registerNib(nib, forCellReuseIdentifier: "MessageCell")
        
        self.refreshControl = UIRefreshControl()
        self.refreshControl?.backgroundColor = FlatWhite()
        self.refreshControl?.tintColor = FlatSand()
        self.refreshControl?.addTarget(self, action: "refresh", forControlEvents: .ValueChanged)
        
        
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        self.getMessages()
    }
    
    func refresh() {
        self.getMessages()
    }

    private func getMessages() {
        provider.request(DodoApi.ListMessages)
                .mapJSON()
                .mapToObjectArray(Message.self)
                .subscribeNext { [weak weakSelf=self]messages in
                    print("\(messages.count)")
                    weakSelf?.messages = messages
                    weakSelf?.tableView.reloadData()
                    weakSelf?.refreshControl?.endRefreshing()
                
        }
    }
  

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (self.messages?.count) ?? 0
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("MessageCell", forIndexPath: indexPath)

        if let cell = cell as? MessageCell {
            let message = self.messages![indexPath.row]
            cell.type?.text = Constants.messageDict[message.type]
            cell.messageView?.text = message.message
        }

        return cell
    }
    

    
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }
 
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return CGFloat(80)
    }

    
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            let message = self.messages![indexPath.row]
            self.messages?.removeAtIndex(indexPath.row)
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
            provider.request(DodoApi.DeleteMessage(id: String(message.id)))
                    .subscribeNext({ (Response) in
                        print("delete")
                    })
            
        }
    }
 

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
