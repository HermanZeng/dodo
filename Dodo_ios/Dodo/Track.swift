//
//  Track.swift
//  Dodo
//
//  Created by Kevin Ling on 7/22/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Track: JSONAbleType {
    let id: String
    let original_id: String
    var title: String
    var imgURL: String?
    var initiator: String
    var modifier: String?
    let create_date: String
    var fork_cnt: Int
    var star_cnt: Int
    var categories: [Int]
    var stages: [Stage]
    
    init(id: String = "", original_id: String = "", title: String, imgURL: String?, initiator: String="", modifier: String="", create_date: String="", fork_cnt: Int=0, star_cnt: Int=0, categories: [Int], stages: [Stage]) {
        self.id = id
        self.original_id = original_id
        self.title = title
        self.imgURL = imgURL
        self.initiator = initiator
        self.modifier = modifier
        self.create_date = create_date
        self.fork_cnt = fork_cnt
        self.star_cnt = star_cnt
        self.categories = categories
        self.stages = stages
    }
    
    static func fromJSON(json: [String : AnyObject]) -> Track {
        let json = JSON(json)
        let id = json["id"].stringValue
        let original = json["original_id"].stringValue
        let title = json["title"].stringValue
        let imgURL = json["imgURL"].stringValue
        let initiator = json["initiator"].stringValue
        let modifier = json["modifier"].stringValue
        let create_date = json["create_date"].stringValue
        let fork_cnt = json["fork_cnt"].intValue
        let star_cnt = json["star_cnt"].intValue
        
        var categories = [Int]()
        var stages = [Stage]()
        
        if let cates = json["category"].array {
            for ca in cates {
                categories.append(ca.intValue)
            }
        }
        if let sta = json["stage"].array {
            for st in sta {
                stages.append(Stage.fromJSON(st.dictionaryObject!))
            }
        }
        
        return Track(id: id, original_id: original, title: title, imgURL: imgURL, initiator: initiator, modifier: modifier, create_date: create_date, fork_cnt: fork_cnt, star_cnt: star_cnt, categories: categories, stages: stages)
    }
    
    func create_body() -> [String: AnyObject] {
        var body = [String: AnyObject]()
        body["title"] = self.title
        body["image"] = self.imgURL
        body["category"] = self.categories
        body["stage"] = self.stages.map { $0.create_body()}
        
        return body
    }
    
    func getForkString() -> String {
        return "Fork: \(self.fork_cnt)"
    }
    
    func getStarString() -> String {
        return "Star: \(self.star_cnt)"
    }
    
    func sortInPlace() {
        self.stages.sortInPlace {
            $0.seq < $1.seq
        }
    }
    
}
