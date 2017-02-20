package dao.impl;

import dao.CommentDAO;
import entity.CommentTrack;
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
public class HibernateCommentTrackDAO implements CommentDAO<CommentTrack> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommentTrack save(CommentTrack entity) throws ExecuteException {
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
            logger.error("HibernateCommentTrackDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save comment error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentTrack find(long id) throws ExecuteException, NotFoundException {
        CommentTrack entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (CommentTrack) session.get(CommentTrack.class, id);

            if (entity == null) {
                logger.trace("HibernateCommentTrackDAO: comment not found");
                throw new NotFoundException("No such comment (by id)");
            }
        } catch (HibernateException e) {
            logger.error("HibernateCommentTrackDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentTrackDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentTrack update(long id, CommentTrack entity) throws ExecuteException, NotFoundException {
        Session session = null;
        CommentTrack old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (CommentTrack) session.get(CommentTrack.class, id);
            if (old == null) {
                throw new NotFoundException("HibernateCommentTrackDAO: update: comment not found by id");
            }

            old.setContent(entity.getContent());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentTrackDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update comment error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        CommentTrack old = find(id);

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("HibernateCommentTrackDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete comment error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<CommentTrack> listByReviewId(long reviewId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<CommentTrack>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from CommentTrack comment where comment.reviewId=? order by likeCnt desc");
            query.setLong(0, reviewId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentTrackDAO: listByReviewId: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentTrackDAO listByReviewId error");
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

            CommentTrack old = (CommentTrack) session.get(CommentTrack.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }
            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_comment_track VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentTrackDAO: like: hibernate error");
            throw new ExecuteException("HibernateCommentTrackDAO like error");
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

            CommentTrack old = (CommentTrack) session.get(CommentTrack.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_comment_track WHERE user_id='"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentTrackDAO: cancel like: hibernate error");
            throw new ExecuteException("HibernateCommentTrackDAO cancel like error");
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

            Query query = session.createSQLQuery("SELECT * FROM like_comment_track WHERE user_id=\'"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentTrackDAO: likeExist: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentTrackDAO likeExist error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }

//    public static void main(String[] args) {
////        TrackReview review = new TrackReview();
////        review.setReviewer("58bc8a7d-469e-11e6-bf08-208984f5a994");
////        review.setContent("adf嚄iewfjjfw2方2");
////        review.setTrackId("5791d99d799d5ab652f173b9");
//
//        HibernateCommentTrackDAO dao = new HibernateCommentTrackDAO();
//        dao.cancelLike("29c303f4-250a-4482-9ce0-338f07db3d00", 1);
//    }
}
