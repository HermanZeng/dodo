//
//  SingleTrackViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/14/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import Alamofire

class SingleTrackViewController: UITableViewController {

    var books:[String: Book]?
    var bookProgress:[String: Progress]?
    var track:Track?
    var trackProgress: TrackProgress?
    var provider: Networking?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let nib = UINib(nibName: "TrackTableSectionHeader", bundle: nil)
        self.tableView.registerNib(nib, forHeaderFooterViewReuseIdentifier: "TrackTableSectionHeader")

        alreadyRead?.text = String(self.trackProgress!.alreadyRead)
        totalBooks?.text = String(self.trackProgress!.totalBooks)
        trackTitle?.text = self.track?.title
        progressCircle?.angle = (self.trackProgress?.percent)! * 360
        progress?.text = "\(Int((self.trackProgress?.percent)! * 100))%"
        
        currentStage?.text = (self.trackProgress?.current_stage)!
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return self.track?.stages.count ?? 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        if let stages = self.track?.stages {
            for stage in stages {
                if stage.seq == section + 1 {
                    return stage.bookIds.count
                }
            }
        }
        return 0
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("BookCell", forIndexPath: indexPath)
        if let cell = cell as? BookCell {
            var stage: Stage?
            if let stages = self.track?.stages {
                for stage_ in stages {
                    if stage_.seq == indexPath.section + 1 {
                        stage = stage_
                    }
                }
            }
            let book = (self.books![(stage?.bookIds[indexPath.row])!])!
            
            cell.currentStreak?.text = String(0)
            let bookId = book.id
            cell.titleView?.text = book.title
            if let progress = self.bookProgress![bookId] {
                cell.totalPagesView?.text = String(progress.totalPages)
                let pages = progress.currentPage ?? 0
                let total = progress.totalPages ?? 500
                
                
                
                cell.pagesReadView?.text = String(pages)
                cell.percentageView?.text = "\(Int(progress.percent * 100))%"
                cell.circularProgressView?.angle = progress.percent * 360
                
                let index = progress.date.rangeOfString(" ")?.startIndex ?? progress.date.endIndex
                cell.lastUpdateView?.text = progress.date.substringToIndex(index)
            }
            
        }
        return cell
    }
    
    override func tableView(tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var stage: Stage?
        if let stages = self.track?.stages {
            for stage_ in stages {
                if stage_.seq == section + 1 {
                    stage = stage_
                }
            }
        }
        let title = stage?.honor_title ?? ""
        
        // Dequeue with the reuse identifier
        let cell = self.tableView.dequeueReusableHeaderFooterViewWithIdentifier("TrackTableSectionHeader")
        let header = cell as! TrackTableSectionHeader
        header.sectionLabel.text = title
        
        return cell
    }

    @IBOutlet weak var alreadyRead: UILabel!

    @IBOutlet weak var totalBooks: UILabel!

    @IBOutlet weak var trackTitle: UILabel!
    @IBOutlet weak var progressCircle: KDCircularProgress!
    @IBOutlet weak var progress: UILabel!
    @IBOutlet weak var currentStage: UILabel!
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        performSegueWithIdentifier("Show SingleBook", sender: self)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "Show SingleBook" {
            if let indexPath = tableView.indexPathForSelectedRow {
                var stage: Stage?
                if let stages = self.track?.stages {
                    for stage_ in stages {
                        if stage_.seq == indexPath.section + 1 {
                            stage = stage_
                        }
                    }
                }
                let bookId = stage?.bookIds[indexPath.row]
                let book = self.books![bookId!]
                let progress = self.bookProgress![book!.id]


                //let cover = self.bookCover[book.id]
                
                if let nvc = segue.destinationViewController as? SingleBookViewController {
                    nvc.book = book
                    nvc.bookProgress = progress
                    nvc.provider = self.provider
                }
            }
        }

    }
    @IBAction func editOption(sender: UIBarButtonItem) {
        let optionMenu = UIAlertController(title: nil, message: "Choose Option", preferredStyle: .ActionSheet)
        
        let EditAction = UIAlertAction(title: "Edit Track", style: .Default, handler:
            {
                (alert: UIAlertAction!) -> Void in
                self.removeFromLibrary()
        })
        let addAction = UIAlertAction(title: "remove from my library", style: .Default, handler:
            {
                (alert: UIAlertAction!) -> Void in
                self.removeFromLibrary()
        })
        let starAction = UIAlertAction(title: "star", style: .Default, handler:
            {
                (alert: UIAlertAction!) -> Void in
                self.starTrack()
        })
        let commentAction = UIAlertAction(title: "comment", style: .Default, handler:
            {
                (alert: UIAlertAction!) -> Void in
                self.showComments()
        })
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler:
            {
                (alert: UIAlertAction!) -> Void in
                print("Cancel")
        })
        optionMenu.addAction(EditAction)
        optionMenu.addAction(addAction)
        optionMenu.addAction(starAction)
        optionMenu.addAction(commentAction)
        optionMenu.addAction(cancelAction)
        self.presentViewController(optionMenu, animated: true, completion: nil)
    }
    private func editTrack() {
        
    }
    
    private func removeFromLibrary() {
        provider!.request(DodoApi.DeleteTrack(id: self.track!.id))
            .subscribeNext { (Response) in
                print("successfully deleted")
                self.navigationController?.popViewControllerAnimated(true)
        }
    }
    
    private func starTrack() {
        provider!.request(DodoApi.StarTrack(id: self.track!.id))
            .subscribeNext { (Response) in
                print("successfully star")
        }
    }
    
    private func showComments() {
        // TODO
    }
}
