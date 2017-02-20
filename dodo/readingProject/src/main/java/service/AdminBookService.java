package service;

import dao.BookDAO;
import entity.Book;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Created by heming on 7/13/2016.
 */
public class AdminBookService {
    private BookDAO bookDAO;

    public BookDAO getBookDAO() {
        return bookDAO;
    }

    public void setBookDAO(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book addBook(Book book) {
        Book newBook = null;
        String bookId = UUID.randomUUID().toString();
        book.setId(bookId);

        try {
            newBook = bookDAO.save(book);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Add book error: " + ee.getMessage());
        } catch (ConflictException ce) {
            ce.printStackTrace();
            throw new exception.service.ConflictException("Add book error: book already exists" + ce.getMessage());
        }

        return newBook;
    }

    public void deleteBook(String bookId) {
        try {
            bookDAO.delete(bookId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Delete book error:" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("Delete book error: book not found" + ne.getMessage());
        }
    }

    public Book updateBook(String bookId, Book book) {
        Book updatedBook = null;
        if (book.getWid() == null) {
            book.setWid(bookId);
        }

        try {
            updatedBook = bookDAO.update(bookId, book);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Update book error:" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("Update book error: book not found" + ne.getMessage());
        }
        return updatedBook;
    }

    public List<Book> findAllBook(int start, int count) {
        List<Book> books = null;

        try {
            books = bookDAO.findAll(start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("find all user error" + ee.getMessage());
        }

        return books;
    }

    public Book findBookById(String bookId) {
        Book book = null;

        try {
            book = bookDAO.find(bookId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("findBookById error:" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("Find book error: book not found" + ne.getMessage());
        }

        return book;
    }

    public Book findBookByIsbn(String isbn) {
        Book book = null;

        if (isbn.length() != 10 && isbn.length() != 13) {
            throw new exception.service.ExecuteException("findBookByIsbn error: ISBN format is incorrect");
        }
        try {
            book = bookDAO.findByIsbn(isbn);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("findBookByIsbn error:" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("Find book error: book not found" + ne.getMessage());
        }

        return book;
    }
}
