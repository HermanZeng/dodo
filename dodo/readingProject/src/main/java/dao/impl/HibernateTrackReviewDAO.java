package dao.impl;

import dao.TrackDAO;
import dao.TrackReviewDAO;
import entity.Track;
import entity.TrackReview;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;
import utilities.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 9/11/2016.
 */
public class HibernateTrackReviewDAO implements TrackReviewDAO {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public TrackReview save(TrackReview entity) throws ExecuteException {
        Session session = null;
        try {
            entity.setLikeCnt(0);
            entity.setDate(TimeUtil.getCurrentDate());

            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.save(entity);

            tx.commit();

        } catch (HibernateException he) {
            logger.error("HibernateTrackReviewDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save track review error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public TrackReview find(long id) throws ExecuteException, NotFoundException {
        TrackReview entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (TrackReview) session.get(TrackReview.class, id);

            if (entity == null) {
                logger.trace("HibernateTrackReviewDAO: track review not found");
                throw new NotFoundException("No such track review (by id)");
            }
        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateTrackReviewDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public TrackReview update(long id, TrackReview entity) throws ExecuteException, NotFoundException {
        Session session = null;
        TrackReview old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (TrackReview) session.get(TrackReview.class, id);
            if (old == null) {
                throw new NotFoundException("HibernateTrackReviewDAO: update: track review not found by id");
            }

            old.setContent(entity.getContent());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update track review error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        TrackReview old = find(id);

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete track review error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<TrackReview> listByReviewer(String userId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<TrackReview>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from TrackReview review where review.reviewer=? order by review.date desc ");
            query.setString(0, userId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: listByReviewer: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateTrackReviewDAO listByReviewer error");
        } finally {
            session.close();
        }
        return ret;
    }

    @Override
    public List<TrackReview> listByTrackId(String trackId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<TrackReview>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from TrackReview review where review.trackId=? order by likeCnt desc");
            query.setString(0, trackId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: listByTrackId: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateTrackReviewDAO listByTrackId error");
        } finally {
            session.close();
        }
        return ret;
    }

    @Override
    public void like(String userId, long id) throws ConflictException, ExecuteException {
        if (likeExists(userId, id)) {
            throw new ConflictException("cannot like twice.");
        }

        Session session = null;
        Transaction tx = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            tx = session.beginTransaction();

            TrackReview old = (TrackReview) session.get(TrackReview.class, id);
            if (old == null) {
                throw new ExecuteException("review not found");
            }
            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_trackreview VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateTrackReviewDAO: like: hibernate error");
            throw new ExecuteException("HibernateTrackReviewDAO like error");
        } finally {
            session.close();
        }
    }

    @Override
    public void cancelLike(String userId, long id) throws NotFoundException, ExecuteException {
        if (!likeExists(userId, id)) {
            throw new NotFoundException("previous 'like' not found.");
        }

        Session session = null;
        Transaction tx = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            tx = session.beginTransaction();

            TrackReview old = (TrackReview) session.get(TrackReview.class, id);
            if (old == null) {
                throw new ExecuteException("review not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_trackreview WHERE user_id='"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateTrackReviewDAO: cancel like: hibernate error");
            throw new ExecuteException("HibernateTrackReviewDAO cancel like error");
        } finally {
            session.close();
        }
    }

    private boolean likeExists(String userId, long id) {
        Session session = null;
        List ret;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createSQLQuery("SELECT * FROM like_trackreview WHERE user_id=\'"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateTrackReviewDAO: likeExist: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateTrackReviewDAO likeExist error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }


}
