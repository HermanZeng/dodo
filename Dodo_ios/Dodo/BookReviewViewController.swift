//
//  ReviewViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 9/12/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import ChameleonFramework

class BookReviewViewController: UITableViewController {

    var provider = Networking.newDefaultNetworking()
    var book: Book?
    var reviews: [LongBookReview]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.getReviews()
        self.refreshControl = UIRefreshControl()
        self.refreshControl?.backgroundColor = FlatWhite()
        self.refreshControl?.tintColor = FlatSand()
        self.refreshControl?.addTarget(self, action: "refresh", forControlEvents: .ValueChanged)

    }
    
    func refresh() {
        self.getReviews()
    }
    
    private func getReviews() {
        let id = self.book!.id
//        self.provider.request(DodoApi.ListBookReviews(id: id))
//            .mapJSON()
//            .mapToObjectArray(LongBookReview.self)
//            .subscribeNext { [weak weakSelf=self]reviews in
//                weakSelf?.reviews = reviews
//                weakSelf?.tableView.reloadData()
//                weakSelf?.refreshControl?.endRefreshing()
//        }
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.reviews?.count ?? 0
    }
    
    @IBAction func addReview(sender: UIBarButtonItem) {
    }
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("BookReviewCell", forIndexPath: indexPath)
        if let cell = cell as? BookReviewCell {
            let review = self.reviews![indexPath.row]
            cell.reviewerView.text = review.title
            cell.likeNumberView.text = (String(review.like_cnt))
            cell.reviewView.text = review.content
            
            return cell
        }
        return cell
    }
}
