package dao.impl;

import entity.Author;
import exception.dao.ExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fan on 9/12/2016.
 */
public class HibernateAuthorDAO {
    private static final Logger logger = LogManager.getLogger();

    public List<Author> findByName(String name, int start, int count) {
        Session session = null;
        List<Author> authors = new LinkedList<Author>();
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT id, name, nationality, introduction, image FROM author WHERE name like \'%" + name + "%\' limit " + start +
                    ", " + count + ";";
            Query query = session.createSQLQuery(sql);

            List<Object[]> res = query.list();
            for (Object[] tuple : res) {
                Author author = new Author();
                author.setId((String) tuple[0]);
                author.setName((String) tuple[1]);
                author.setNationality((String) tuple[2]);
                author.setIntroduction((String) tuple[3]);
                author.setImage((String) tuple[4]);

                authors.add(author);
            }

            tx.commit();
        } catch (HibernateException he) {
            logger.error("find author by name error");
            he.printStackTrace();
            throw new ExecuteException("find author by name error");
        } finally {
            session.close();
        }
        return authors;
    }

//    public static void main(String[] args) {
//        HibernateAuthorDAO dao = new HibernateAuthorDAO();
//        System.out.println(dao.findByName("钱", 0 ,5));
//    }
}
