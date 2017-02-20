package service;

import dao.BookDAO;
import entity.Book;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heming on 7/22/2016.
 */
public class UserBookService {
    private BookDAO bookDAO;

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public List<Book> searchBookByTitle(String title, int start, int count) {
        List<Book> bookList = null;

        try {
            bookList = bookDAO.searchByTitle(title, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Search book by title error" + ee.getMessage());
        }

        return bookList;
    }

    public List<Book> searchBookByIsbn(String isbn, int start, int count) {
        List<Book> bookList = null;

        try {
            bookList = bookDAO.searchByIsbn(isbn, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Search book by isbn error" + ee.getMessage());
        }

        return bookList;
    }

    public List<Book> searchBookByCategory(int category, int start, int count) {
        List<String> books = null;

        try {
            books = bookDAO.searchByCategory(category, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("find bookIds error in searchBookByCategory: " + ee.getMessage());
        }

        return this.bulkFind(books);
    }

    public List<Book> bulkFind(List<String> books) {
        List<Book> bookList = new ArrayList<Book>();

        for (String bookId : books
                ) {
            try {
                Book book = bookDAO.find(bookId);
                bookList.add(book);
            } catch (ExecuteException ee) {
                ee.printStackTrace();
                throw new exception.service.ExecuteException("Search book by category error: " + ee.getMessage());
            } catch (NotFoundException ne) {

            }

        }

        return bookList;
    }
}
