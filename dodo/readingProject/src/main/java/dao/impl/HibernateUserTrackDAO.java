package dao.impl;

import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;
import utilities.HibernateUtil;

import java.util.List;

/**
 * Created by fan on 7/20/2016.
 */
public class HibernateUserTrackDAO {
    private static final Logger logger = LogManager.getLogger();

    public void save(String userId, String trackId, Integer origin) throws ExecuteException {
        String user_id = "\'" + userId + "\'";
        String track_id = "\'" + trackId + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "insert into user_track(user_id, track_id, origin) values("
                    + user_id
                    + ", "
                    + track_id
                    + ", "
                    + origin
                    + ");";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (ConstraintViolationException e) {
            throw new IllegalInputException("UserTrackDAO save error: already forked");
        } catch (HibernateException he) {
            logger.error("UserTrackDAO save error");
            he.printStackTrace();
            throw new ExecuteException("UserTrackDAO save error");
        } finally {
            session.close();
        }
    }

    public void delete(String userId, String trackId) throws ExecuteException {
        String user_id = "\'" + userId + "\'";
        String track_id = "\'" + trackId + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "DELETE FROM user_track WHERE user_id="
                    + user_id
                    + " AND track_id="
                    + track_id
                    + ";";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("UserTrackDAO delete error");
            he.printStackTrace();
            throw new ExecuteException("UserTrackDAO delete error");
        } finally {
            session.close();
        }
    }

    public List<String> findAll(String userId, int start, int count) throws ExecuteException {
        List<String> results = null;
        String user_id = "\'" + userId + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT track_id FROM user_track WHERE user_id="
                    + user_id
                    + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("UserTrackDAO findAll error");
            he.printStackTrace();
            throw new ExecuteException("UserTrackDAO findAll error");
        } finally {
            session.close();
        }
        return results;
    }

    public List<String> findPartial(String userId, boolean origin, int start, int count) throws ExecuteException {
        List<String> results = null;
        String user_id = "\'" + userId + "\'";
        int originVal = 0;
        if (origin) originVal = 1;

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "SELECT track_id FROM user_track WHERE user_id="
                    + user_id
                    + " AND origin="
                    + originVal
                    + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("UserTrackDAO findPartial error");
            he.printStackTrace();
            throw new ExecuteException("UserTrackDAO findPartial error");
        } finally {
            session.close();
        }
        return results;
    }

}
