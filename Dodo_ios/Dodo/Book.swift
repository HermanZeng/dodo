//
//  Book.swift
//  Dodo
//
//  Created by Kevin Ling on 7/19/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import SwiftyJSON

final class Category: JSONAbleType {
    let id: String
    let description: String

    init(id: String, description: String) {
        self.id = id
        self.description = description
    }
    
    static func fromJSON(json: [String: AnyObject]) -> Category {
        let json = JSON(json)
        
        let id = json["id"].stringValue
        let description = json["description"].stringValue
        
        return Category(id: id, description: description)
    }
}

final class Book: JSONAbleType {
    
    let authors: [Author]?
    let id: String
    var imgURL: String?
    let introduction: String
    var numberOfPages: Int?
    let publisher: String
    var rate: Double?
    let isbn: String
    var categories: [Category]?
    let title: String
    var translators: [Author]?
    let wid: String
    
    init(authors: [Author], id: String, imgURL: String?, introduction: String, numberOfPages: Int?, publisher: String, rate: Double?, isbn: String, categories: [Category]?, title: String, translators: [Author]?, wid: String) {
        self.authors = authors
        self.id = id
        self.imgURL = imgURL
        self.introduction = introduction
        self.numberOfPages = numberOfPages
        self.publisher = publisher
        self.rate = rate
        self.isbn = isbn
        self.categories = categories
        self.title = title
        self.translators = translators
        self.wid = wid
    }

    
    static func fromJSON(json: [String: AnyObject]) -> Book {
        let json = JSON(json)
        
        let id = json["id"].stringValue
        var authors = [Author]()
        if let arrayOfAuthors = json["authors"].array {
            for author in arrayOfAuthors {
                authors.append(Author.fromJSON(author.dictionaryObject!))
            }
        }
        var translators = [Author]()
        if let arrayOfTrans = json["translators"].array {
            for author in arrayOfTrans {
                translators.append(Author.fromJSON(author.dictionaryObject!))
            }
        }
        let imgURL = json["image"].stringValue
        let introduction = json["introduction"].stringValue
        let numberOfPages = json["pages"].intValue
        let publisher = json["publisher"].stringValue
        let rate = json["rate"].doubleValue
        let isbn = json["isbn"].stringValue
        var categories = [Category]()
        
        if let arrayOfCategories = json["categories"].array {
            for cate in arrayOfCategories {
                categories.append(Category.fromJSON(cate.dictionaryObject!))
            }
        }
        let title = json["title"].stringValue
        let wid = json["wid"].stringValue
        
        return Book(authors: authors, id: id, imgURL: imgURL, introduction: introduction, numberOfPages: numberOfPages, publisher: publisher, rate: rate, isbn: isbn, categories: categories, title: title, translators: translators, wid: wid)
    }
    
    func getAllCategories() -> String {
        let ret = self.categories?.reduce("", combine: { (prev, cate) -> String in
            return prev + cate.description + " "
        })
        return ret ?? ""
    }

}
extension Book: Equatable {}

func == (lhs: Book, rhs: Book) -> Bool {
    return lhs.id == rhs.id
}

