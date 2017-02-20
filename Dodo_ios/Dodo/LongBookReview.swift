//
//  LongBookReview.swift
//  Dodo
//
//  Created by Kevin Ling on 9/12/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON
final class LongBookReview: JSONAbleType {
    var id: String
    var wid: String
    var reviewer: String
    var title: String
    var content: String
    var like_cnt: Int
    var date: String
    
    init(id: String, wid: String, reviewer: String, content: String, like_cnt: Int, date: String, title: String) {
        self.id = id
        self.wid = wid
        self.reviewer = reviewer
        self.content = content
        self.like_cnt = like_cnt
        self.date = date
        self.title = title
    }
    
    static func fromJSON(json: [String : AnyObject]) -> LongBookReview {
        let json = JSON(json)
        
        let id = json["id"].stringValue
        let wid = json["wid"].stringValue
        let reviewer = json["reviewer"].stringValue
        let content = json["content"].stringValue
        let like_cnt = json["like_cnt"].intValue
        let date = json["date"].stringValue
        let title = json["title"].stringValue
        
        return LongBookReview(id: id, wid: wid, reviewer: reviewer, content: content, like_cnt: like_cnt, date: date, title: title)
    }
}