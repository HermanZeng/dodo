package test;


import entity.Book;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;

import java.util.List;

/**
 * Created by heming on 7/22/2016.
 */
public class HibernateLikeTest {

    public static void main(String args[]) {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        String sql = "from Book as book where book.title like :title";

        Query query = session.createQuery(sql);

        query.setParameter("title", "%"+"hjdsshhdjkshhk"+"%");
        List<Book> bookList =  query.list();
        System.out.println(bookList.isEmpty());

        tx.commit();

        for (Book book: bookList
             ) {
            System.out.println(book.getTitle());
        }


        session.close();

        //query = session.createQuery("from Book book where book.isbn10=?");
    }

}
