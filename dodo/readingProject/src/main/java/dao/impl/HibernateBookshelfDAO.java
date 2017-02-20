package dao.impl;

import dao.BookshelfDAO;
import dao.ReadingProgressDAO;
import entity.Book;
import entity.ReadingProgress;
import entity.User;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;

import java.util.Set;

/**
 * Created by heming on 7/13/2016.
 */
public class HibernateBookshelfDAO implements BookshelfDAO {

    @Override
    public Book save(String userId, String bookId) throws ExecuteException, ConflictException, NotFoundException {
        ReadingProgressDAO progressDAO = new HibernateReadingProgressDAO();

        User user = null;
        Book book = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            user = (User) session.load(User.class, userId);
            book = (Book) session.get(Book.class, bookId);
            if (user == null) {
                throw new NotFoundException("User does not exist");
            }
            if (book == null) {
                throw new NotFoundException("Book does not exist");
            }
            //Set<Book> bookSet = user.getBooks();
            if (user.getBooks().contains(book)) {
                throw new ConflictException("Book already exists in shelf");
            } else {
                //bookSet.add(book);
                user.getBooks().add(book);
            }

            //user.setBooks(bookSet);
            session.update(user);
            tx.commit();

            // add to book shelf means a new reading progress begins
            ReadingProgress newProgress = new ReadingProgress();
            newProgress.setUserId(userId);
            newProgress.setBookId(bookId);
            newProgress.setTotal(book.getPages());

            progressDAO.save(newProgress);

        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: save book in bookshelf failed" + he.getMessage());
        } finally {
            session.close();
        }
        return book;
    }

    @Override
    public void delete(String userId, String bookId) throws ExecuteException, NotFoundException {
        User user = null;
        Book book = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            user = (User) session.get(User.class, userId);
            book = (Book) session.get(Book.class, bookId);
            if (user == null) {
                throw new NotFoundException("User does not exist");
            }
            if (book == null) {
                throw new NotFoundException("Book does not exist");
            }
            Set<Book> bookSet = user.getBooks();
            if (bookSet.contains(book)) {
                bookSet.remove(book);
            }

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: save book in bookshelf failed" + he.getMessage());
        } finally {
            session.close();
        }
    }
}
