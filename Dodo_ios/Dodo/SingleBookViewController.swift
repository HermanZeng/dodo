//
//  SingleBookViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/14/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import Cosmos
import SCLAlertView
import Alamofire

class SingleBookViewController: UIViewController {

    var book: Book?
    var bookCover: UIImage?
    var bookProgress: Progress?
    var provider: Networking?
    
    var currentPage: Int = 0
    var totalPage: Int = 500
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let url = book?.imgURL {
            Alamofire.request(.GET, url)
                .responseImage(completionHandler: { [weak weakSelf=self] response in
                    weakSelf?.bookCover = response.result.value
                    })
        }
        
        self.view.dodo.style.bar.hideAfterDelaySeconds = 3
        
        self.bookTitle?.text = self.book?.title
        self.bookAuthor?.text = self.book?.authors?.reduce("", combine: { (prev: String, author: Author) -> String in
            prev + author.name
        })
        self.bookTranslator?.text = self.book?.translators?.reduce("", combine: { (prev: String, author: Author) -> String in
            prev + author.name
        })
        self.bookCoverView?.image = self.bookCover
        self.currentPage = self.bookProgress?.currentPage ?? 0
        self.totalPage = self.bookProgress?.totalPages ?? 500

        self.circularView?.angle = (self.bookProgress?.percent)! * 360
        self.progressNumberView?.text = "\(Int((self.bookProgress?.percent)! * 100))%"
        

        self.currentPageView?.text = String(self.currentPage)
        self.totalPageView?.text = String(self.totalPage)
        
        self.introView?.text = self.book?.introduction
        self.rateView?.rating = Double(self.book?.rate ?? 0) / 2
        print("\(self.book?.rate)")
        // Do any additional setup after loading the view.
        if let url = book?.imgURL {
            Alamofire.request(.GET, url)
                .responseImage(completionHandler: { [weak weakSelf=self] response in
                    weakSelf?.bookCover = response.result.value
                    weakSelf?.bookCoverView?.image = weakSelf?.bookCover
                    })
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    private func updateProgress(newPage: String) {
        if let number = Int(newPage) {
            if number <= self.totalPage && number >= 0 && number != self.currentPage{
                updateCurrentPage(number)
                return
            }
        }
        self.view.dodo.error("Invalid Page Number! Try Again!")
    }
    
    private func updateCurrentPage(newPage: Int) {
        self.currentPage = newPage
        self.bookProgress?.currentPage = newPage
        self.circularView?.angle = (self.bookProgress?.percent)! * 360
        self.progressNumberView?.text = "\(Int((self.bookProgress?.percent)! * 100))%"
        self.currentPageView?.text = String(self.currentPage)
        provider!.request(DodoApi.UpdateBookProgress(bookId: self.book!.id, currentPage: newPage))
                .subscribeNext { (Response) in
                    print("updated")
        }
    }
    private func updateTotalPage(total: String) {
        if let num = Int(total) {
            if validTotalPageNumber(num) {
                self.totalPage = num
                self.bookProgress?.totalPages = num
                self.circularView?.angle = (self.bookProgress?.percent)! * 360
                self.progressNumberView?.text = "\(Int((self.bookProgress?.percent)! * 100))%"
                self.totalPageView?.text = String(self.totalPage)
                provider!.request(DodoApi.SetTotalPage(bookId: self.book!.id, numberOfPages: num))
                    .subscribeNext { (Response) in
                        print("updated totalPages")
                }
            } else {
                self.view.dodo.error("Invalid Number of Total Pages!")
            }
        }
    }
    func validTotalPageNumber(pageNum: Int) -> Bool {
        return pageNum > 0
    }
    
    @IBOutlet weak var bookTitle: UILabel!
    @IBOutlet weak var bookAuthor: UILabel!
    @IBOutlet weak var bookTranslator: UILabel!
    @IBOutlet weak var bookCoverView: UIImageView!
    @IBOutlet weak var circularView: KDCircularProgress!
    @IBOutlet weak var progressNumberView: UILabel!
    @IBOutlet weak var currentStreakView: UILabel!

    @IBOutlet weak var currentPageView: UILabel!
    @IBOutlet weak var totalPageView: UILabel!
    @IBAction func updatePage(sender: UIButton) {
        let appearance = SCLAlertView.SCLAppearance(showCloseButton: false)
        let alert = SCLAlertView(appearance: appearance)
        let txt = alert.addTextField("Enter the page number")
        alert.addButton("Done") {
            self.updateProgress(txt.text ?? "")
        }
        alert.addButton("Close") {
            
        }
        alert.showEdit("Update Progress", subTitle:"Enter the current page you're at")
    }
    @IBOutlet weak var introView: UITextView!
    @IBOutlet weak var rateView: CosmosView!

    
    @IBAction func addRemark(sender: UIButton) {
        let storyboard = UIStoryboard(name: "BookReviewRoot", bundle: nil)
        let vc = storyboard.instantiateViewControllerWithIdentifier("BookReviewViewController") as! BookReviewViewController
        vc.book = self.book
        self.presentViewController(vc, animated: true, completion: nil)
    }
    @IBAction func removeBook(sender: UIButton) {
        let appearance = SCLAlertView.SCLAppearance(showCloseButton: false)
        let alert = SCLAlertView(appearance: appearance)
        alert.addButton("Yes") {
            self.doRemove()
        }
        alert.addButton("Cancel") {
        }
        alert.showWarning("Update Progress", subTitle:"Are you sure you want to delete this book from your bookshelf?")
    }
    
    @IBAction func setTotalNumberOfPages(sender: UIButton) {
        let appearance = SCLAlertView.SCLAppearance(showCloseButton: false)
        let alert = SCLAlertView(appearance: appearance)
        let txt = alert.addTextField("Enter the number of pages")
        alert.addButton("Done") {
            self.updateTotalPage(txt.text ?? "")
        }
        alert.addButton("Close") {
            
        }
        alert.showEdit("Update Book Pages", subTitle:"Total Number of Pages")
    }
    
    private func doRemove() {
        provider!.request(DodoApi.DeleteBook(bookId: self.book!.id))
                .subscribeNext { _ in
                    self.navigationController?.popToRootViewControllerAnimated(true)
                    print("deleted")
        }
    }
}
