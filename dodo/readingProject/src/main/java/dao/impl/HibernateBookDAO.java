package dao.impl;

import dao.BookDAO;
import entity.Book;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;

import java.util.List;

/**
 * Created by heming on 7/11/2016.
 */
public class HibernateBookDAO implements BookDAO {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Book save(Book book) throws ExecuteException, ConflictException {
        Book already = null;
        try {
            if (book.getIsbn10().length() == 10) {
                already = findByIsbn(book.getIsbn10());
            } else if (book.getIsbn10().length() == 13) {
                already = findByIsbn(book.getIsbn13());
            }
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new ExecuteException("Save book error");
        } catch (NotFoundException ne) {
            Session session = null;
            try {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                session = sf.openSession();
                Transaction tx = session.beginTransaction();

                session.save(book);

                tx.commit();
            } catch (HibernateException he) {
                logger.error("bookDAO: save: hibernate error");
                he.printStackTrace();
                throw new ExecuteException("Save book error");
            } finally {
                session.close();
            }
            return book;
        }
        throw new ConflictException("Book already exists!");
    }

    @Override
    public void delete(String id) throws ExecuteException, NotFoundException {
        Book book = find(id);
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.delete(book);

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: delete book");
        } finally {
            session.close();
        }
    }

    @Override
    public Book update(String id, Book entity) throws ExecuteException, NotFoundException {
        Book book = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            book = (Book) session.get(Book.class, id);

            if (book == null) {
                throw new NotFoundException("BookDAO: Can't find book by id");
            }

            book.setPages(entity.getPages());
            book.setWid(entity.getWid());
            book.setTitle(entity.getTitle());
            book.setPublisher(entity.getPublisher());
            book.setIntroduction(entity.getIntroduction());
            book.setRate(entity.getRate());
            book.setImage(entity.getImage());
            book.setIsbn10(entity.getIsbn10());
            book.setIsbn13(entity.getIsbn13());
            book.setAuthors(entity.getAuthors());
            book.setTranslators(entity.getTranslators());
            book.setCategories(entity.getCategories());

            session.update(book);

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: update user error");
        } finally {
            session.close();
        }

        return book;
    }

    @Override
    public Book find(String id) throws ExecuteException, NotFoundException {
        Book book = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from Book book where book.id=?");
            query.setString(0, id);
            book = (Book) query.uniqueResult();
            tx.commit();

            if (book == null) {
                throw new NotFoundException("No such book (by id)");
            }
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("BookDAO: Find book by id error");
        } finally {
            session.close();
        }
        return book;
    }

    @Override
    public List<Book> findAll(int start, int count) throws ExecuteException {
        List<Book> bookList = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM Book book order by book.rate");
            query.setFirstResult(start);
            query.setMaxResults(count);
            bookList = query.list();

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("List books error");
        } finally {
            session.close();
        }
        return bookList;
    }

    @Override
    public Book findByIsbn(String isbn) throws ExecuteException, NotFoundException {
        Book book = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();
            Query query = null;

            if (isbn.length() == 10) {
                query = session.createQuery("from Book book where book.isbn10=?");
            } else if (isbn.length() == 13) {
                query = session.createQuery("from Book book where book.isbn13=?");
            }

            query.setString(0, isbn);
            book = (Book) query.uniqueResult();
            tx.commit();

            if (book == null) {
                throw new NotFoundException("No such book (by ISBN)");
            }
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("BookDAO: findByIsbn: hibernate error");
        } finally {
            session.close();
        }

        return book;
    }

    @Override
    public List<Book> searchByTitle(String title, int start, int count) throws ExecuteException {
        List<Book> bookList = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "from Book as book where book.title like :title order by book.rate desc";
            Query query = session.createQuery(sql);
            query.setParameter("title", "%" + title + "%");
            query.setFirstResult(start);
            query.setMaxResults(count);
            bookList = query.list();

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("BookDAO: searchBookByTitle: hibernate error" + he.getMessage());
        } finally {
            session.close();
        }

        return bookList;
    }

    @Override
    public List<Book> searchByIsbn(String isbn, int start, int count) throws ExecuteException {
        List<Book> bookList = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "from Book as book where book.isbn10 like :isbn10 or book.isbn13 like :isbn13 order by book.rate desc";
            Query query = session.createQuery(sql);
            query.setParameter("isbn10", "%" + isbn + "%");
            query.setParameter("isbn13", "%" + isbn + "%");
            query.setFirstResult(start);
            query.setMaxResults(count);
            bookList = query.list();

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("BookDAO: searchBookByIsbn: hibernate error" + he.getMessage());
        } finally {
            session.close();
        }

        return bookList;
    }

    @Override
    public List<String> searchByCategory(int category, int start, int count) throws ExecuteException {
        List<String> results = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT book_id FROM book_category WHERE category_id="
                    + category
                    + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("HibernateBookDAO error: searchByCategory: " + he.getMessage());
        } finally {
            session.close();
        }

        return results;
    }


}
