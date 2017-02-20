package service;

import dao.impl.HibernateBookshelfDAO;
import dao.impl.HibernateReadingProgressDAO;
import dao.impl.HibernateUserDAO;
import entity.Book;
import entity.User;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import security.token.TokenManager;

import java.util.Set;

/**
 * Created by heming on 7/13/2016.
 */
public class BookshelfService {
    private HibernateUserDAO hibernateUserDAO;
    private HibernateBookshelfDAO hibernateBookshelfDAO;
    private HibernateReadingProgressDAO progressDAO;
    private TokenManager tokenManager;

    public Book addBook(String token, String bookId) {
        String userId = tokenManager.getUserId(token);
        Book book = null;

        try {

            book = hibernateBookshelfDAO.save(userId, bookId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Hibernate error in add book to shelf" + ee.getMessage());
        } catch (NotFoundException ne) {
            throw new exception.service.NotFoundException("User or book does not exist" + ne.getMessage());
        } catch (ConflictException ce) {
            throw new exception.service.ConflictException("Book already exists in bookshelf" + ce.getMessage());
        }

        return book;
    }

    public void deleteBook(String token, String bookId) {
        String userId = tokenManager.getUserId(token);

        try {
            hibernateBookshelfDAO.delete(userId, bookId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Hibernate error in delete book in shelf" + ee.getMessage());
        } catch (NotFoundException ne) {
            throw new exception.service.NotFoundException("User or book does not exist" + ne.getMessage());
        }
    }

    public Set<Book> listAll(String token) {
        String userId = tokenManager.getUserId(token);
        User user = null;
        Set<Book> bookList = null;

        try {
            user = hibernateUserDAO.find(userId);
            bookList = user.getBooks();
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("Hibernate error in list all books" + ee.getMessage());
        } catch (NotFoundException ne) {
            throw new exception.service.NotFoundException("User does not exist" + ne.getMessage());
        }

        return bookList;
    }

    public HibernateUserDAO getHibernateUserDAO() {
        return hibernateUserDAO;
    }

    public void setHibernateUserDAO(HibernateUserDAO hibernateUserDAO) {
        this.hibernateUserDAO = hibernateUserDAO;
    }

    public HibernateBookshelfDAO getHibernateBookshelfDAO() {
        return hibernateBookshelfDAO;
    }

    public void setHibernateBookshelfDAO(HibernateBookshelfDAO hibernateBookshelfDAO) {
        this.hibernateBookshelfDAO = hibernateBookshelfDAO;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
