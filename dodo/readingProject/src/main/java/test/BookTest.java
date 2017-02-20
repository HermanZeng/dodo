package test;

import com.alibaba.fastjson.JSON;
import entity.Author;
import entity.Book;
import entity.Role;
import entity.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;

import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by heming on 7/8/2016.
 */
public class BookTest {
    public static void main(String[] args) {
        //SessionFactory sf = new Configuration().configure().buildSessionFactory();
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Book where id= :id");
        //UUID uuid = UUID.fromString("0185e06f-4686-43ea-be73-3d927be2b6b8");
        query.setParameter("id", "044e2a85-c595-434d-b8cd-8d4f7fbf221b");
        Book book = (Book) query.uniqueResult();

        /*Query query = session.createQuery("from Role where id = :id");
        query.setInteger("id", 1);
        Role role = (Role) query.uniqueResult();*/

//        String json = JSON.toJSONString(book);
//        System.out.println(json);

        tx.commit();
        /*for (Author a:
                book.getAuthors()) {
            a.setBooks(null);
        }*/
        System.out.println(book);


        session.close();
//        System.out.println(book.getAuthors().size());
//        for (Author a : book.getAuthors()) {
//            System.out.println(a);
//        }


        //System.out.println(role.getDescription());

        /*System.out.println(user.getEmail());
        Iterator it = user.getRoles().iterator();
        while (it.hasNext()) {
            Role obj = (Role)it.next();
            System.out.println(obj.getDescription());
        }*/
//        System.out.println(book);
//        for (Author a:
//             book.getAuthors()) {
//            a.setBooks(null);
//        }
//        String json = JSON.toJSONString(book);
//        System.out.println(json);
//
//        Book book1 = JSON.parseObject(json, Book.class);
//        System.out.println(book1);
    }
}
