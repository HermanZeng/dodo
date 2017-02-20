//
//  TracksViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/10/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import XLPagerTabStrip
import ChameleonFramework

class TracksViewController: UITableViewController, IndicatorInfoProvider {

    var colors = [UIColor.greenColor(), UIColor.redColor(), UIColor.blueColor(), UIColor.brownColor()]
    var tracks = [Track]()
    var provider: Networking!
    var bookProgress = [String: Progress]()
    var book = [String: Book]()
    var trackProgress = [String: TrackProgress]()
    
    var itemInfo: IndicatorInfo = "Tracks"
    class func instantiateFromStoryboard() -> TracksViewController {
        let storyboard = UIStoryboard(name: "LibraryItems", bundle: nil)
        return storyboard.instantiateViewControllerWithIdentifier(String(self)) as! TracksViewController
    }
    
    func indicatorInfoForPagerTabStrip(pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return itemInfo
    }
    
    override func viewDidLoad() {
        self.tableView.backgroundColor = UIColor(red: 237, green: 239, blue: 240, alpha: 0.0)
        provider = Networking.newDefaultNetworking()
        
        self.refreshControl = UIRefreshControl()
        self.refreshControl?.backgroundColor = FlatWhite()
        self.refreshControl?.tintColor = FlatSand()
        self.refreshControl?.addTarget(self, action: "refresh", forControlEvents: .ValueChanged)
        self.refresh()
    }
    
    func refresh() {
        self.getTracks()
    }
    
    private func getTracks() {
        provider.request(DodoApi.ListTrack)
                .mapJSON()
                .mapToObjectArray(Track.self)
                .subscribeNext { [weak weakSelf = self]tracks in
                    weakSelf?.tracks = tracks
                    weakSelf?.getBooks()
        }
    }
    
    private func getBooks() {
        provider.request(DodoApi.ListBooksOnBookShelf)
                .mapJSON()
                .mapToObjectArray(Book.self)
                .subscribeNext { [weak weakSelf=self]books in
                    for book in books {
                        weakSelf?.book[book.id] = book
                    }
                    weakSelf?.getBookProgress()
        }
    }
    
    private func getBookProgress() {
        provider.request(DodoApi.ListBooksProgress)
            .mapJSON()
            .mapToObjectArray(Progress.self)
            .subscribeNext { [weak weakSelf=self] progresses in
                for progress in progresses {
                    weakSelf?.bookProgress[progress.bookId] = progress
                }
                weakSelf?.refreshControl?.endRefreshing()
                weakSelf?.calculateTrackProgress()
        }
    }
    
    private func calculateTrackProgress() {
        for track in self.tracks {
            var totalPage = 0
            var readedPage = 0
            var totalBooks = 0
            var alreadyRead = 0
            var current_stage_idx = 0
            track.stages.sortInPlace({ (stage1, stage2) -> Bool in
                return stage1.seq < stage2.seq
            })
            for stage in track.stages {
                var finished = true
                totalBooks += stage.bookIds.count
                for bookId in stage.bookIds {
                    let progress = self.bookProgress[bookId]
                    totalPage += (progress?.totalPages) ?? 1
                    readedPage += (progress?.currentPage) ?? 0
                    if progress?.currentPage != progress?.totalPages {
                        finished = false
                        print("false")
                    } else {
                        alreadyRead += 1
                    }
                }
                if finished && current_stage_idx < track.stages.count-1 {
                    current_stage_idx += 1
                }
            }
            var current_stage = track.stages[current_stage_idx].honor_title
            
            self.trackProgress[track.id] = TrackProgress(ttBooks: totalBooks, alRead: alreadyRead, ttPages: totalPage, alReadPages: readedPage, current: current_stage)
            
        }
        self.tableView.reloadData()
    }

}


extension TracksViewController {
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tracks.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("TrackCell", forIndexPath: indexPath)
        if let cell = cell as? TrackCell {
            let track = self.tracks[indexPath.row]
            let progress = self.trackProgress[track.id]
            
            cell.numOfBooksView?.text = String((progress?.totalBooks)!)
            cell.nameView?.text = track.title
            cell.alreadyRead?.text = String((progress?.alreadyRead)!)
            cell.progressCircle?.angle = (progress?.percent)! * 360
            cell.progress?.text = "\(Int((progress?.percent)! * 100))%"
            
            cell.currentStage?.text = (progress?.current_stage)!
        }
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        performSegueWithIdentifier("Show Track", sender: self)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "Show Track" {
            if let indexPath = tableView.indexPathForSelectedRow {
                let track = self.tracks[indexPath.row]
                let progress = self.trackProgress[track.id]
                
                if let nvc = segue.destinationViewController as? SingleTrackViewController {
                    nvc.track = track
                    nvc.books = self.book
                    nvc.provider = self.provider
                    nvc.trackProgress = progress
                    nvc.bookProgress = self.bookProgress
                }
            }
        }

    }
}