package dao.impl;

import entity.Translator;
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
public class HibernateTranslatorDAO {
    private static final Logger logger = LogManager.getLogger();

    public List<Translator> findByName(String name, int start, int count) {
        Session session = null;
        List<Translator> translators = new LinkedList<Translator>();
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT id, name, nationality, introduction, image FROM translator WHERE name like \'%" + name + "%\' limit " + start +
                    ", " + count + ";";
            Query query = session.createSQLQuery(sql);

            List<Object[]> res = query.list();
            for (Object[] tuple : res) {
                Translator translator = new Translator();
                translator.setId((String) tuple[0]);
                translator.setName((String) tuple[1]);
                translator.setNationality((String) tuple[2]);
                translator.setIntroduction((String) tuple[3]);
                translator.setImage((String) tuple[4]);

                translators.add(translator);
            }

            tx.commit();
        } catch (HibernateException he) {
            logger.error("find author by name error");
            he.printStackTrace();
            throw new ExecuteException("find author by name error");
        } finally {
            session.close();
        }
        return translators;
    }

//    public static void main(String[] args) {
//        HibernateTranslatorDAO dao = new HibernateTranslatorDAO();
//        System.out.println(dao.findByName("钱", 0 ,5));
//    }
}
