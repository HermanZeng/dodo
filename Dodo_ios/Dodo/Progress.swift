//
//  Progress.swift
//  Dodo
//
//  Created by Kevin Ling on 7/19/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Progress: JSONAbleType {
    
    let bookId: String
    let userId: String
    var totalPages: Int
    var currentPage: Int
    var date: String
    
    var percent: Double {
        if self.totalPages > 0 {
            return Double(self.currentPage) / Double(self.totalPages)
        } else {
            return 0.0
        }
    }
    
    init(bookid: String, userid: String, totalPages: Int, currentPage: Int, date: String) {
        self.bookId = bookid
        self.userId = userid
        self.totalPages = totalPages
        self.currentPage = currentPage
        self.date = date
    }
    
    static func fromJSON(json: [String : AnyObject]) -> Progress {
        let json = JSON(json)
        
        let bkid = json["book_id"].stringValue
        let usid = json["user_id"].stringValue
        let totalPages = json["total"].intValue
        let currentPage = json["current"].intValue
        let date = json["date"].stringValue
        
        return Progress(bookid: bkid, userid: usid, totalPages: totalPages, currentPage: currentPage, date: date)
    }
}
class TrackProgress {
    var totalBooks: Int
    var alreadyRead: Int
    
    var totalPages: Int
    var alreadyReadPages: Int
    
    var current_stage: String
    
    
    var percent: Double {
        return Double(self.alreadyReadPages) / Double(self.totalPages)
    }
    
    init(ttBooks: Int, alRead: Int, ttPages: Int, alReadPages: Int, current: String) {
        self.totalBooks = ttBooks
        self.alreadyRead = alRead
        self.totalPages = ttPages
        self.alreadyReadPages = alReadPages
        self.current_stage = current
    }
}
