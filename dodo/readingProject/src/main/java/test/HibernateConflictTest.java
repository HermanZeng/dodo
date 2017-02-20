package test;

import dao.impl.HibernateBookDAO;
import dao.impl.HibernateUserDAO;
import entity.Book;
import entity.User;
import exception.dao.NotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;

/**
 * Created by heming on 7/13/2016.
 */
public class HibernateConflictTest {
    public static void main(String args[]) {
//        HibernateUserDAO hibernateUserDAO = new HibernateUserDAO();
//        HibernateBookDAO hibernateBookDAO = new HibernateBookDAO();
//
//        User user = hibernateUserDAO.find("58bc8a7d-469e-11e6-bf08-208984f5a994");
//        Book book = hibernateBookDAO.find("fca9a39e-05da-45ee-9f5c-ea92ee82dd4c");
//
//        user.getBooks().add(book);
//
//        hibernateUserDAO.update(user.getId(), user);
        sole();
    }

    public static void sole() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        Book book = (Book) session.get(Book.class, "fca9a39e-05da-45ee-9f5c-ea92ee82dd4c");
        User user = (User) session.get(User.class, "58bc8a7d-469e-11e6-bf08-208984f5a994"); // not use userDAO.find() because it would erase user's books
        user.getBooks().add(book);

        session.update(user);

        tx.commit();
    }
}
