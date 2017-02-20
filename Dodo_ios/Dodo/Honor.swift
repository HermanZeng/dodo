//
//  Honor.swift
//  Dodo
//
//  Created by Kevin Ling on 9/12/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Honor: JSONAbleType {
    var id: Int64
    var userId: String
    var trackId: String
    var stageSeq: Int
    var title: String
    
    init(id: Int64, userId: String, trackId: String, stageSeq: Int, title: String) {
        self.id = id
        self.userId = userId
        self.trackId = trackId
        self.stageSeq = stageSeq
        self.title = title
    }
    static func fromJSON(json: [String : AnyObject]) -> Honor {
        let json = JSON(json)
        
        let id = json["id"].int64Value
        let userId = json["userId"].stringValue
        let trackId = json["trackId"].stringValue
        let title = json["title"].stringValue
        let stageS = json["stageSeq"].intValue
        return Honor(id: id, userId: userId, trackId: trackId, stageSeq: stageS, title: title)
    }
}