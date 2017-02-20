package dao.impl;

import exception.dao.ExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;

import java.util.List;

/**
 * Created by fan on 7/20/2016.
 */
public class HibernateTrackCategoryDAO {
    private static final Logger logger = LogManager.getLogger();

    public void save(Integer categoryRef, String trackId, String trackTitle) throws ExecuteException {
        String tableName = "category_" + categoryRef;
        String id = "\'" + trackId + "\'";
        String title = "\'" + trackTitle + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "insert into " + tableName + "(track_id, title) values("
                    + id
                    + ", "
                    + title
                    + ");";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("trackCategoryDAO: save: error");
            he.printStackTrace();
            throw new ExecuteException("Save track category error");
        } finally {
            session.close();
        }
    }

    public void delete(Integer categoryRef, String trackId) throws ExecuteException {
        String tableName = "category_" + categoryRef;
        String id = "\'" + trackId + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "DELETE FROM " + tableName + " WHERE track_id="
                    + id
                    + ";";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("trackCategoryDAO: delete: error");
            he.printStackTrace();
            throw new ExecuteException("delete track category error");
        } finally {
            session.close();
        }
    }

    public List<String> findAll(Integer categoryRef, int start, int count) throws ExecuteException {

        List<String> results = null;
        String tableName = "category_" + categoryRef;

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT track_id FROM " + tableName + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("trackCategoryDAO: list: error");
            he.printStackTrace();
            throw new ExecuteException("list track category error");
        } finally {
            session.close();
        }
        return results;
    }

    public List<String> findByTitle(Integer categoryRef, String trackTitle, int start, int count) throws ExecuteException {

        List<String> results = null;
        String tableName = "category_" + categoryRef;
        String titleQuery = "\'%" + trackTitle + "%\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT track_id FROM " + tableName + " Where title like "
                    + titleQuery
                    + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("trackCategoryDAO: list: error");
            he.printStackTrace();
            throw new ExecuteException("list track category error");
        } finally {
            session.close();
        }
        System.out.println(results);
        return results;
    }
}
