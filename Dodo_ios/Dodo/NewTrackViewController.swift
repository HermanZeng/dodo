//
//  NewTrackViewController.swift
//  Dodo
//
//  Created by Kevin Ling on 7/14/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import UIKit
import Eureka

class NewTrackViewController:  FormViewController {
    
    var stage_counts: Int = 0
    var book_counts = [Int]()
    var stages = [Stage]()
    var booksDict = [String: String]()
    var provider: Networking!
    
    
    
    func stageByIndex(index: Int) -> Section {
        let tag = "Stage \(index)"
        return self.form.sectionByTag(tag)!
    }
    
//    func bookByIndex(stageIndex: Int, index: Int) -> Row {
//        let tag = "Stage \(stageIndex) Row \(index)"
//        return self.form.rowByTag(tag)!
//    }

    override func viewDidLoad() {
        super.viewDidLoad()
        provider = Networking.newDefaultNetworking()
        form +++ Section("Track Profile")
            <<< TextRow(){ row in
                row.tag = "title"
                row.title = "Track Name"
                row.placeholder = "Enter name here"
            }
            <<< MultipleSelectorRow<String>("Categories") {
                $0.title = "Select categories"
                $0.tag = "categories"
                $0.options = ["文学", "流行", "文化", "生活", "经管", "科技", "学习"]
            }
        
            +++ Section()
            <<< ButtonRow() { (row: ButtonRow) -> Void in
                row.title = "Add Stage"
                }  .onCellSelection({ (cell, row) in
                    let section = self.newStage()
                    self.form.insert(section, atIndex: (row.indexPath()?.section)!)
                })
            <<< ButtonRow() { (row: ButtonRow) -> Void in
                row.title = "Delete Stage"
                }  .onCellSelection({ (cell, row) in
                    if self.stage_counts > 0 {
                        if let section = self.form.sectionByTag("Stage \(self.stage_counts)") {
                            self.form.removeAtIndex((section.index)!)
                            self.stage_counts -= 1
                        }
                    }
                })

        
        // Do any additional setup after loading the view.
    }
    

    func newStage() -> Section {
        self.stage_counts += 1
        if self.book_counts.count < stage_counts {
            self.book_counts.append(0)
        }
        let sectionTitle = "Stage \(self.stage_counts)"
        var section = Section(sectionTitle)
        section.tag = sectionTitle
        section.append(TextRow() { row in
                row.title = "Honor Title"
                row.tag = "Stage \(self.stage_counts) title"
            })
        section
            <<< ButtonRow() { (row: ButtonRow) -> Void in
                row.title = "Add Book"
                } .onCellSelection({ (cell, row) in
                    let row = BookRow("Stage \((section.index)!) book \(self.book_counts[section.index!-1])")
                    print("\(row.tag)")
                    section.insert(row, atIndex: 1 + self.book_counts[section.index!-1])
                    self.book_counts[section.index!-1] += 1
                })

        return section
    }
    
    func newBook(section: Section) -> ButtonRow {
        let bookRow = ButtonRow() {(row: ButtonRow) -> Void in
            row.title = "Click to search for books"
        } .onCellSelection { (cell, row) in
            self.performSegueWithIdentifier("Search Book", sender: self)
        }
        
        return bookRow
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "Search Book" {
            if let nvc = segue.destinationViewController as? AddBookViewController {
//                nvc.delegate = self
                
            }
        }
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func new(sender: UIBarButtonItem) {
        if let section = form.sectionByTag("hello") {
            print("hah")
            section <<< TextRow() {
            row in
            row.title = "new"
            row.placeholder = "new row"
            }
            
        }
    }

    
//    func tableView(tableView: UITableView, editActionsForRowAtIndexPath indexPath: NSIndexPath) -> [UITableViewRowAction]? {
//        if indexPath.section > 0 && indexPath.row > 0 {
//            var actions = [UITableViewRowAction]()
//            let action = UITableViewRowAction(style: .Destructive, title: "Delete", handler: { (action, indexPath) -> Void in
//            })
//            actions.append(action)
//            return actions
//        }
//        return []
//    }
    
    func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // valid book index
        return indexPath.section > 0 && indexPath.row > 0 && indexPath.section <= self.stage_counts && indexPath.row <= self.book_counts[indexPath.section-1]
    }
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            var section = self.stageByIndex(indexPath.section)
            section.removeAtIndex(indexPath.row)
        }
    }
    
    @IBAction func doneCreating(sender: UIBarButtonItem) {
        let titleRow: TextRow? = form.rowByTag("title")
        let trackTitle = titleRow!.value
        
        let values = form.values()
        let categ : Set<String> = values["categories"].value as! Set<String>
        let categories = Array(categ).map { (Constants.category[$0])!}
        var stages = [Stage]()
        for i in 1...self.stage_counts {
            let seq = i
            let honor_title = values["Stage \(i) title"].value as! String
            var bookIds = [String]()
            for (tag, row) in values {
                if tag.containsString("Stage \(i) book") {
                    let book = row as! Book
                    bookIds.append(book.id)
                }
            }
            var stage = Stage(honor_title: honor_title, seq: seq, bookIds: bookIds)
            stages.append(stage)
        }
        let track = Track(title: trackTitle!, imgURL: nil, categories: categories, stages: stages)
        print("\(track.stages.first?.bookIds)")
        provider.request(DodoApi.CreateTrack(track: track))
                .subscribeNext { [weak weakSelf=self](Response) in
                    print("created successfully")
                    weakSelf?.navigationController?.popViewControllerAnimated(true)
        }
    }

}


//custom row type for select book
final class BookRow : SelectorRow<Book, PushSelectorCell<Book>, AddBookViewController>, RowType {
    public required init(tag: String?) {
        super.init(tag: tag)
        presentationMode = .Show(controllerProvider: ControllerProvider.Callback { return
            AddBookViewController(){_ in } }, completionCallback: { vc in vc.navigationController?.popViewControllerAnimated(true) })
        displayValueFor = {
            guard let _book = $0 else { return "" }
            let book = _book as Book
            return "\(book.title)"
        }
    }
}




























