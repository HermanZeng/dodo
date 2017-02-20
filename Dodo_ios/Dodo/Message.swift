//
//  Message.swift
//  Dodo
//
//  Created by Kevin Ling on 9/11/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Message: JSONAbleType {
    var id: Int64
    var userId: String
    var message: String
    var read: Int
    var date: String
    var type: Int
    var reqId: String
    
    init(id: Int64, userId: String, message: String, read: Int, date: String, type: Int, reqId: String) {
        self.userId = userId
        self.message = message
        self.date = date
        self.read = read
        self.type = type
        self.reqId = reqId
        self.id = id
    }
    static func fromJSON(json: [String : AnyObject]) -> Message {
        let json = JSON(json)
        let userId = json["userId"].stringValue
        let message = json["message"].stringValue
        let read = json["read"].intValue
        let date = json["date"].stringValue
        let type = json["type"].intValue
        let reqId = json["reqId"].stringValue
        let id = json["id"].int64Value
        return Message(id: id, userId: userId, message: message, read: read, date: date, type: type, reqId: reqId)
    }
    
}