//
//  TrackViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/10/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import XLPagerTabStrip
import AlamofireImage
import Alamofire
import SCLAlertView
import ChameleonFramework

class BooksViewController: UITableViewController, IndicatorInfoProvider {
    
    var provider: Networking!
    var books = [Book]()

    var isRefreshing: Bool = false
    var progress = [String: Progress]()
    var bookCover = [String: UIImage]()
    var itemInfo: IndicatorInfo = "Books"
    class func instantiateFromStoryboard() -> BooksViewController {
        let storyboard = UIStoryboard(name: "LibraryItems", bundle: nil)
        return storyboard.instantiateViewControllerWithIdentifier(String(self)) as! BooksViewController
    }
    
    func indicatorInfoForPagerTabStrip(pagerTabStripController: PagerTabStripViewController) -> IndicatorInfo {
        return itemInfo
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        provider = Networking.newDefaultNetworking()
        
        getBooks()
        getProgress()
        
        self.refreshControl = UIRefreshControl()
        self.refreshControl?.backgroundColor = FlatWhite()
        self.refreshControl?.tintColor = FlatSand()
        self.refreshControl?.addTarget(self, action: "refresh", forControlEvents: .ValueChanged)
    }

    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        getProgress()
    }
    private func getBooks() {
        provider.request(DodoApi.ListBooksOnBookShelf)
            .mapJSON()
            .mapToObjectArray(Book.self)
            .subscribeNext { [weak weakSelf = self]books in
                weakSelf?.books = books
                weakSelf?.getCover()
                weakSelf?.tableView.reloadData()
        }
    }
    
    private func getProgress() {
        provider.request(DodoApi.ListBooksProgress)
            .mapJSON()
            .mapToObjectArray(Progress.self)
            .subscribeNext { [weak weakSelf=self] progresses in
                for progress in progresses {
                    weakSelf?.progress[progress.bookId] = progress
                }
                weakSelf?.tableView.reloadData()
                weakSelf?.refreshControl?.endRefreshing()
        }
    }
    private func getCover() {
        for book in self.books {
            if let url = book.imgURL {
                Alamofire.request(.GET, url)
                         .responseImage(completionHandler: { [weak weakSelf=self] response in
                            weakSelf?.bookCover[book.id] = response.result.value
                         })
            }
        }
    }
    
    func refresh() {
        getBooks()
        getProgress()
        getCover()
    }
}



extension BooksViewController {
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.books.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("BookCell", forIndexPath: indexPath)
        if let cell = cell as? BookCell {
            cell.currentStreak?.text = String(0)
            let bookId = self.books[indexPath.row].id
            cell.titleView?.text = self.books[indexPath.row].title
            if let progress = self.progress[bookId] {
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
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        performSegueWithIdentifier("Show Book", sender: self)
    }
    
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "Show Book" {
            if let indexPath = tableView.indexPathForSelectedRow {
                let book = self.books[indexPath.row]
                let progress = self.progress[book.id]
                let cover = self.bookCover[book.id]
                
                if let nvc = segue.destinationViewController as? SingleBookViewController {
                    nvc.book = book
                    nvc.bookProgress = progress
                    nvc.bookCover = cover
                    nvc.provider = self.provider
                }
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}