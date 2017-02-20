//
//  SearchSingleViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/18/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import Cosmos
import SCLAlertView

class SearchSingleBookViewController: UIViewController {

    var provider: Networking?
    var book: Book?
    var bookCover: UIImage?
    override func viewDidLoad() {
        self.navigationController?.setNavigationBarHidden(false, animated: false)
        
        self.view.dodo.style.bar.hideAfterDelaySeconds = 3
        
        self.bookTitle?.text = self.book?.title
        self.bookAuthor?.text = self.book?.authors?.reduce("", combine: { (prev: String, author: Author) -> String in
            prev + author.name
        })
        self.bookTranslator?.text = self.book?.translators?.reduce("", combine: { (prev: String, author: Author) -> String in
            prev + author.name
        })
        self.bookCoverView?.image = self.bookCover

        
        self.intro?.text = self.book?.introduction
        self.authorIntro?.text = self.book?.authors?.first?.introduction
        self.rate?.rating = Double(self.book?.rate ?? 0) / 2
        print("\(self.book?.rate)")
        // Do any additional setup after loading the view.

    }
    
    @IBOutlet weak var bookTitle: UILabel!
    @IBOutlet weak var bookAuthor: UILabel!
    @IBOutlet weak var bookTranslator: UILabel!

    @IBOutlet weak var intro: UITextView!
    @IBOutlet weak var authorIntro: UITextView!
    @IBOutlet weak var rate: CosmosView!
    @IBAction func addBook(sender: UIButton) {
        let bookId = self.book!.id
        self.provider!.request(DodoApi.AddBookToShelf(bookId: bookId))
            .subscribeNext { (Response) in
                print("Added")
                self.view.dodo.success("This Book has been added to your library! Check it!")
        }
        
    }
    @IBAction func comment(sender: UIButton) {
        let storyboard = UIStoryboard(name: "BookReviewRoot", bundle: nil)
        let vc = storyboard.instantiateViewControllerWithIdentifier("BookReviewViewController") as! BookReviewViewController
        vc.book = self.book
        self.presentViewController(vc, animated: true, completion: nil)

    }
    @IBOutlet weak var bookCoverView: UIImageView!
}

class SearchSingleTrackViewController: UITableViewController {
    
    var track: Track?
    var books = [String: Book]()
    var provider: Networking?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.track?.sortInPlace()
        self.getBooks()
        trackTitle?.text = self.track?.title
        trackAuthor?.text = self.track?.initiator
        fork?.text = String(self.track?.fork_cnt) ?? "0"
        star?.text = String(self.track?.star_cnt) ?? "0"
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        getBooks()
    }
    
    private func getBooks() {
        for stage in self.track!.stages {
            for bookId in stage.bookIds {
                provider!.request(DodoApi.GetBook(id: bookId))
                        .mapJSON()
                        .mapToObject(Book.self)
                        .subscribeNext({ [weak weakSelf=self]book in
                            weakSelf?.books[bookId] = book
                            print("\(book.title)")
                            weakSelf?.tableView.reloadData()
                        })
            }
        }
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (self.track?.stages[section].bookIds.count)!
    }
    
    override func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return (self.track?.stages[section].honor_title)!
    }
    
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return (self.track?.stages.count)!
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCellWithIdentifier("Track_BookCell", forIndexPath: indexPath)
        if let cell = cell as? Track_BookCell {
            let bookId = self.track!.stages[indexPath.section].bookIds[indexPath.row]
                if let book = self.books[bookId] {
                    cell.titleView?.text = book.title
                    return cell
                }
        }
        return cell
    }
    
    
    @IBOutlet weak var trackTitle: UILabel!
    @IBOutlet weak var trackAuthor: UILabel!
    @IBOutlet weak var fork: UILabel!
    @IBOutlet weak var star: UILabel!
    @IBAction func action(sender: UIBarButtonItem) {
        let optionMenu = UIAlertController(title: nil, message: "Choose Option", preferredStyle: .ActionSheet)
        
        let addAction = UIAlertAction(title: "fork to my library", style: .Default, handler:
            {
                (alert: UIAlertAction!) -> Void in
                self.addToLibrary()
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
        optionMenu.addAction(addAction)
        optionMenu.addAction(starAction)
        optionMenu.addAction(commentAction)
        optionMenu.addAction(cancelAction)
        self.presentViewController(optionMenu, animated: true, completion: nil)
    }
    
    private func addToLibrary() {
        provider!.request(DodoApi.ForkTrack(id: self.track!.id))
                 .subscribeNext { (Response) in
                    print("successfully added")
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
