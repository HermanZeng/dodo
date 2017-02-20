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
public class HibernateUserTrackStarDAO {
    private static final Logger logger = LogManager.getLogger();

    public void save(String userId, String trackId) throws IllegalInputException, ExecuteException {
        String user_id = "\'" + userId + "\'";
        String track_id = "\'" + trackId + "\'";

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            String sql = "insert into user_track_star(user_id, track_id) values("
                    + user_id
                    + ", "
                    + track_id
                    + ");";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (ConstraintViolationException e) {
            throw new IllegalInputException("UserTrackStarDAO save error: already starred");
        } catch (HibernateException he) {
            logger.error("UserTrackStarDAO save error: " + he.getMessage());
            he.printStackTrace();
            throw new ExecuteException("UserTrackStarDAO save error: " + he.getMessage());
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

            String sql = "DELETE FROM user_track_star WHERE user_id="
                    + user_id
                    + " AND track_id="
                    + track_id
                    + ";";
            Query query = session.createSQLQuery(sql);

            query.executeUpdate();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("UserTrackStarDAO delete error: " + he.getMessage());
            he.printStackTrace();
            throw new ExecuteException("UserTrackStarDAO delete error: " + he.getMessage());
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

            String sql = "SELECT track_id FROM user_track_star WHERE user_id="
                    + user_id
                    + " LIMIT "
                    + start
                    + ", "
                    + count;
            Query query = session.createSQLQuery(sql);

            results = query.list();

            tx.commit();
        } catch (HibernateException he) {
            logger.error("UserTrackStarDAO findAll error: " + he.getMessage());
            he.printStackTrace();
            throw new ExecuteException("UserTrackStarDAO findAll error: " + he.getMessage());
        } finally {
            session.close();
        }
        return results;
    }
}
