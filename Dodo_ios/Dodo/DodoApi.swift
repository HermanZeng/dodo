//
//  DodoApi.swift
//  Dodo
//
//  Created by Kevin Ling on 7/5/16.
//  Copyright © 2016 sjtu. All rights reserved.
//

import Foundation
import RxSwift
import Moya

protocol DodoAPIType {
    var addXAuth: Bool { get }
}

enum DodoApi {
    
    case XAuth(email: String, password: String)
    
    case Register(firstname: String, lastname: String, email: String, password: String)
    case Logout
    
    case SearchBooksByTitle(query: String)
    case SearchBooksByAuthor(query: String)
    case SearchBooksByIsbn(query: String)
    
    case ListBooksOnBookShelf
    case ListBooksProgress
    case UpdateBookProgress(bookId: String, currentPage: Int)
    case DeleteBook(bookId: String)
    case SetTotalPage(bookId: String, numberOfPages: Int)
    case AddBookToShelf(bookId: String)
    case GetBook(id: String)
    
    // Track
    case ListTrack
    case CreateTrack(track: Track)
    case DeleteTrack(id: String)
    case EditTrack(track: Track)
    case SearchTracks(query: [String: String])
    case ForkTrack(id: String)
    case StarTrack(id: String)
    case PullRequest(id: String)
    
    case ListMessages
    case UpdateMessage(id: String)
    case DeleteMessage(id: String)
    
    case ListHonors
//    case ListBookReviews(id: String)
//    case AddBookReview(wid: String, title: String, content: String)
}

extension DodoApi : TargetType, DodoAPIType {
    var path: String {
        switch self {
        case .XAuth:
            return "/auth/tokens"
        case .Register:
            return "/auth/register"
        case .Logout:
            return "/auth/tokens"
        case .SearchBooksByTitle:
            return "/book/search/title"
        case .SearchBooksByIsbn:
            return "/book/search/isbn"
        case .SearchBooksByAuthor:
            return "/book/search/author"
            
        case .GetBook(let id):
            return "/book/\(id)"
        case .ListBooksOnBookShelf:
            return "/bookshelf/books"
        case .ListBooksProgress:
            return "/progress"
        case .UpdateBookProgress(let bookId, let pageNum):
            return "/progress/\(bookId)/\(pageNum)"
        case .DeleteBook(let bookId):
            return "/bookshelf/books/\(bookId)"
        case .SetTotalPage(let bookId, let num):
            return "/progress/\(bookId)/total/\(num)"
        case .AddBookToShelf(let bookId):
            return "/bookshelf/books/\(bookId)"
        
        
            // track
        case .ListTrack:
            return "/track"
        case .CreateTrack:
            return "/track"
        case .DeleteTrack(let id):
            return "/track/\(id)"
        case .EditTrack(let track):
            return "/track/\(track.id)"
        case .SearchTracks:
            return "/track/search"
        case .ForkTrack(let id):
            return "/track/fork/\(id)"
        case .StarTrack(let id):
            return "/track/star/\(id)"
        case .PullRequest(let id):
            return "/track/pullrequest/\(id)"
            
        case .ListMessages:
            return "/message"
        case .UpdateMessage(let id):
            return "/message/\(id)"
        case .DeleteMessage(let id):
            return "/message/\(id)"
            
        case .ListHonors:
            return "/honor"
//        case .ListBookReviews(let id):
//            return "/long_book_review/book/\(id)"
//        case .AddBookReview:
//            return "/long_book_review"
        }
    }
    
    var base: String { return Constants.RestApi.BaseURL }
    var baseURL: NSURL { return NSURL(string: base)! }
    
    var parameters: [String: AnyObject]? {
        switch self {
        case .XAuth(let email, let password):
            return [
                "email": email,
                "password": password
            ]
        case .Register(let firstname, let lastname, let email, let password):
            return [
                "firstname": firstname,
                "lastname": lastname,
                "email": email,
                "password": password
            ]
        case .CreateTrack(let track):
            return track.create_body()
        case .EditTrack(let track):
            return track.create_body()
        case .SearchBooksByTitle(let title):
            return ["title": title]
        case .SearchBooksByAuthor(let author):
            return ["author": author]
        case .SearchBooksByIsbn(let isbn):
            return ["isbn": isbn]
            
        case .SearchTracks(let query):
            return query
        
//        case .AddBookReview(let wid, let title, let content):
//            return ["wid":wid, "title":title, "content":content]
        default:
            return nil
        }
    }
    
    var method: Moya.Method {
        switch self {
        case .XAuth, .Register, .UpdateBookProgress, .CreateTrack, .AddBookToShelf,
             .ForkTrack, .StarTrack, .PullRequest:
            return .POST
        case .SetTotalPage, .EditTrack:
            return .PUT
        case .Logout, .DeleteBook, .DeleteTrack, .DeleteMessage:
            return .DELETE
        case .UpdateMessage:
            return .HEAD
        default:
            return .GET
        }
    }
    
    var sampleData: NSData {
        switch self {
        case .XAuth:
            return stubbedResponse("login")
        default:
            return stubbedResponse("testfile")
        }
    }
    
    var multipartBody: [MultipartFormData]? {
        return nil
    }
    var addXAuth: Bool {
        switch self {
        case .Register, .XAuth:
            return false
        default:
            return true
        }
    }
}


func stubbedResponse(filename: String) -> NSData! {
    @objc class TestClass: NSObject { }
    
    let bundle = NSBundle(forClass: TestClass.self)
    let path = bundle.pathForResource(filename, ofType: "json")
    return NSData(contentsOfFile: path!)
}



private extension String {
    var URLEscapedString: String {
        return self.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLHostAllowedCharacterSet())!
    }
}

func url(route: TargetType) -> String {
    return route.baseURL.URLByAppendingPathComponent(route.path).absoluteString
}















