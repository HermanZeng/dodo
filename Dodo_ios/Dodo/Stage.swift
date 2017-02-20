//
//  Stage.swift
//  Dodo
//
//  Created by Kevin Ling on 7/22/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Stage: JSONAbleType {
    var honor_title: String
    let seq: Int
    var bookIds: [String]
    
    init(honor_title: String, seq: Int, bookIds: [String]) {
        self.honor_title = honor_title
        self.seq = seq
        self.bookIds = bookIds
    }
    
    static func fromJSON(json: [String : AnyObject]) -> Stage {
        let json = JSON(json)
        let honor_title = json["honor_title"].stringValue
        let seq = json["seq"].intValue
        
        var bookIDs = [String]()
        
        if let books = json["books"].array {
            for book in books {
                bookIDs.append(book.stringValue)
            }
        }
        return Stage(honor_title: honor_title, seq: seq, bookIds: bookIDs)
    }
    
    func create_body() -> [String: AnyObject] {
        var body = [String: AnyObject]()
        body["honor_title"] = self.honor_title
        body["seq"] = self.seq
        body["books"] = self.bookIds
        return body
    }
}