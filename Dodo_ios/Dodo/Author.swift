//
//  Author.swift
//  Dodo
//
//  Created by Kevin Ling on 7/19/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Author: JSONAbleType {
    
    let id: String
    let introduction: String
    let name: String
    let nationality: String
    
    init(id: String, introduction: String, name: String, nationality: String) {
        self.id = id
        self.introduction = introduction
        self.name = name
        self.nationality = nationality
    }

    static func fromJSON(json: [String: AnyObject]) -> Author {
        let json = JSON(json)
        
        let id = json["id"].stringValue
        let introduction = json["introduction"].stringValue
        let name = json["name"].stringValue
        let nationality = json["name"].stringValue
        
        let author = Author(id: id, introduction: introduction, name: name, nationality: nationality)
        
        return author
    }
}