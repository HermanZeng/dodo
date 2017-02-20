//
//  AddBookViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/22/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import Eureka
import Alamofire
import ChameleonFramework

class AddBookViewController: UITableViewController, TypedRowControllerType {
    
    var provider: Networking!
    
    public var row: RowOf<Book>!
    public var completionCallback: ((UIViewController) -> ())?


    var books = [Book](){
        didSet {
            tableView.reloadData()
        }
    }

    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    public override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: NSBundle?) {
        super.init(nibName: nil, bundle: nil)
    }
    
    convenience public init(_ callback: (UIViewController) -> ()){
        self.init(nibName: nil, bundle: nil)
        completionCallback = callback
    }
    
    var searchText: String? {
        didSet {
            search()
        }
    }
    
    func search() {
        if searchText != "" {
            books.removeAll()
            let search_method = self.searchController.searchBar.selectedScopeButtonIndex
            if search_method == 0 {
                searchForBooks(DodoApi.SearchBooksByTitle(query: searchText!))
            } else if search_method == 1 {
                searchForBooks(DodoApi.SearchBooksByAuthor(query: searchText!))
            } else {
                searchForBooks(DodoApi.SearchBooksByIsbn(query: searchText!))
            }

        }
    }
    
    func searchForBooks(api: DodoApi) {
        provider.request(api)
            .mapJSON()
            .mapToObjectArray(Book.self)
            .subscribeNext { books in
                self.books = books
                self.getCover()
        }
    }
    
    var bookCover = [String: UIImage]() {
        didSet {
            tableView.reloadData()
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
    
    let searchController = UISearchController(searchResultsController: nil)
    override func viewDidLoad() {
        provider = Networking.newDefaultNetworking()
        
        view.dodo.topLayoutGuide = topLayoutGuide
        view.dodo.bottomLayoutGuide = bottomLayoutGuide
        
        view.backgroundColor = UIColor.whiteColor()
        searchController.searchResultsUpdater = self
        searchController.searchBar.delegate = self
        searchController.dimsBackgroundDuringPresentation = false
        
        searchController.searchBar.tintColor = UIColor.whiteColor()
        searchController.searchBar.barTintColor = FlatBlue()
        
        // Setup the Scope Bar
        searchController.searchBar.scopeButtonTitles = ["name","author", "ISBN"]
        tableView.tableHeaderView = searchController.searchBar
        self.definesPresentationContext = true
        searchController.hidesNavigationBarDuringPresentation = false
        view.dodo.style.bar.hideAfterDelaySeconds = 3
        let nib = UINib(nibName: "SearchBookCell", bundle: nil)
        self.tableView.registerNib(nib, forCellReuseIdentifier: "SearchAddBookCell")
        
        
        super.viewDidLoad()
    }
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.books.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCellWithIdentifier("SearchAddBookCell", forIndexPath: indexPath)
        if let cell = cell as? SearchBookCell {
            let bookId = self.books[indexPath.row].id
            cell.titleView?.text = self.books[indexPath.row].title
            cell.coverView?.image = self.bookCover[bookId]
            if let authors = self.books[indexPath.row].authors {
                if authors.count > 0 {
                    cell.authorView?.text = authors[0].name
                }
            }
            if let rate = self.books[indexPath.row].rate {
                cell.rateView?.rating = rate / 2.0
            }
            return cell
        }
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let book = self.books[indexPath.row]
        row.value = book
        completionCallback?(self)
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return CGFloat(120)
    }
}

extension AddBookViewController: UISearchResultsUpdating {
    func updateSearchResultsForSearchController(searchController: UISearchController) {
        searchText = self.searchController.searchBar.text
    }
}

extension AddBookViewController: UISearchBarDelegate {
    func searchBar(searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        
    }
}

