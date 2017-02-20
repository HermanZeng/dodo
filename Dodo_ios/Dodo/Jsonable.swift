//
//  Jsonable.swift
//  Dodo
//
//  Created by Kevin Ling on 7/5/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation

protocol JSONAbleType {
    static func fromJSON(_: [String: AnyObject]) -> Self
}