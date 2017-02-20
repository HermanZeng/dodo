package service;

import entity.Book;
import service.base.Connection;
import service.base.DodoApiRequest;
import org.springframework.http.HttpMethod;

/**
 * Created by fan on 7/13/2016.
 */
public class BooksService {
    private Connection connection;
    private String url;

    public BooksService(String url, Connection connection) {
        this.url = url;
        this.connection = connection;
    }

    public Create create(Book book) {
        book.setId(null);
        book.setRate(null);

        return new Create(book);
    }

    public Delete delete(String bookId) {
        return new Delete(bookId);
    }

    public Update update(String bookId, Book book) {
        book.setId(null);
        book.setRate(null);

        return new Update(bookId, book);
    }

    public List list(int start, int count) {
        return new List(start, count);
    }

    public Show show(String bookId) {
        return new Show(bookId);
    }

    public class Create extends DodoApiRequest<Book, Book> {
        public Create(Book book) {
            super(connection, url, HttpMethod.POST, "", book, Book.class);
        }
    }

    public class Delete extends DodoApiRequest<Void, Void> {
        public Delete(String bookId) {
            super(connection, url, HttpMethod.DELETE, "/" + bookId, null, Void.class);
        }
    }

    public class Update extends DodoApiRequest<Book, Book> {
        public Update(String bookId, Book book) {
            super(connection, url, HttpMethod.PUT, "/" + bookId, book, Book.class);
        }
    }

    public class List extends DodoApiRequest<Void, Book[]> {
        public List(int start, int count) {
            super(connection, url, HttpMethod.GET, "?start=" + start + "&count=" + count, null, Book[].class);
        }
    }

    public class Show extends DodoApiRequest<Void, Book> {
        public Show(String bookId) {
            super(connection, url, HttpMethod.GET, "/" + bookId, null, Book.class);
        }
    }







}
