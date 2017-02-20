//
//  SearchViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/18/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import UIKit
import ChameleonFramework
import Alamofire

class SearchViewController: UITableViewController {
    
    @IBOutlet weak var segDisplayedContent: UISegmentedControl!
    var provider: Networking!
    var books = [Book](){
        didSet {
            tableView.reloadData()
        }
    }
    
    var tracks = [Track](){
        didSet {
            tableView.reloadData()
        }
    }
    
    var searchText: String? {
        didSet {
            search()
        }
    }
    
    func search() {
        if searchText != "" {
            books.removeAll()
            if segDisplayedContent.selectedSegmentIndex == 1 {
                let search_method = self.searchController.searchBar.selectedScopeButtonIndex
                if search_method == 0 {
                    searchForBooks(DodoApi.SearchBooksByTitle(query: searchText!))
                } else if search_method == 1 {
                    searchForBooks(DodoApi.SearchBooksByAuthor(query: searchText!))
                } else {
                    searchForBooks(DodoApi.SearchBooksByIsbn(query: searchText!))
                }
            } else {
                searchForTracks(DodoApi.SearchTracks(query: ["title": searchText!]))
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
                self.tableView.reloadData()
        }
    }
    
    func searchForTracks(api: DodoApi) {
        provider.request(api)
            .mapJSON()
            .mapToObjectArray(Track.self)
            .subscribeNext { tracks in
                self.tracks = tracks
                self.tableView.reloadData()
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
        definesPresentationContext = true
        searchController.dimsBackgroundDuringPresentation = false
        
        searchController.searchBar.tintColor = UIColor.whiteColor()
        searchController.searchBar.barTintColor = FlatBlue()
        
        // Setup the Scope Bar
        searchController.searchBar.scopeButtonTitles = ["name","author"]
        tableView.tableHeaderView = searchController.searchBar
        
        
        view.dodo.style.bar.hideAfterDelaySeconds = 3
        
        
        
        super.viewDidLoad()
    }

    @IBAction func changeContent(sender: UISegmentedControl) {
        if segDisplayedContent.selectedSegmentIndex == 0 {
            self.searchController.searchBar.scopeButtonTitles = ["name","author"]
        } else {
            self.searchController.searchBar.scopeButtonTitles = ["name", "author", "isbn"]
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if self.segDisplayedContent.selectedSegmentIndex == 1 {
            return self.books.count
        } else {
            return self.tracks.count
        }
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if segDisplayedContent.selectedSegmentIndex == 1 {
            let cell = tableView.dequeueReusableCellWithIdentifier("SearchBookCell", forIndexPath: indexPath)
            if let cell = cell as? SearchBookCell {
                let bookId = self.books[indexPath.row].id
                cell.titleView?.text = self.books[indexPath.row].title
                cell.coverView?.image = self.bookCover[bookId]
                cell.categoryView?.text = self.books[indexPath.row].getAllCategories()
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
        } else {
            let cell = tableView.dequeueReusableCellWithIdentifier("SearchTrackCell",
                                                                   forIndexPath: indexPath)
            if let cell = cell as? SearchTrackCell {
                let track = self.tracks[indexPath.row]
                
                cell.titleView?.text = track.title
                cell.forkView?.text = track.getForkString()
                cell.starView?.text = track.getStarString()
                
                return cell
            }
            return cell
        }
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        if (self.segDisplayedContent.selectedSegmentIndex == 1) {
            performSegueWithIdentifier("Show SearchSingleBook", sender: self)
        } else {
            performSegueWithIdentifier("Show SearchSingleTrack", sender: self)
        }
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "Show SearchSingleBook" {
            if let indexPath = tableView.indexPathForSelectedRow {
                let book = self.books[indexPath.row]
                let cover = self.bookCover[book.id]
                
                if let nvc = segue.destinationViewController as? SearchSingleBookViewController {
                    nvc.book = book
                    nvc.bookCover = cover
                    nvc.provider = self.provider
                }
            }
        } else if segue.identifier == "Show SearchSingleTrack" {
            if let indexPath = tableView.indexPathForSelectedRow {
                let track = self.tracks[indexPath.row]
                
                if let nvc = segue.destinationViewController as? SearchSingleTrackViewController {
                    nvc.track = track
                    nvc.provider = self.provider
                }
            }
        }
    }
}

extension SearchViewController: UISearchResultsUpdating {
    func updateSearchResultsForSearchController(searchController: UISearchController) {
        searchText = self.searchController.searchBar.text
    }
}

extension SearchViewController: UISearchBarDelegate {
    func searchBar(searchBar: UISearchBar, selectedScopeButtonIndexDidChange selectedScope: Int) {
        
    }
}
