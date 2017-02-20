package dao.impl;

import dao.CommentDAO;
import entity.CommentLong;
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
public class HibernateCommentLongDAO implements CommentDAO<CommentLong> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommentLong save(CommentLong entity) throws ExecuteException {
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
            logger.error("HibernateCommentLongDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save comment error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentLong find(long id) throws ExecuteException, NotFoundException {
        CommentLong entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (CommentLong) session.get(CommentLong.class, id);

            if (entity == null) {
                logger.trace("HibernateCommentLongDAO: comment not found");
                throw new NotFoundException("No such comment (by id)");
            }
        } catch (HibernateException e) {
            logger.error("HibernateCommentLongDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentLongDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentLong update(long id, CommentLong entity) throws ExecuteException, NotFoundException {
        Session session = null;
        CommentLong old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (CommentLong) session.get(CommentLong.class, id);
            if (old == null) {
                throw new NotFoundException("HibernateCommentLongDAO: update: comment not found by id");
            }

            old.setContent(entity.getContent());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentLongDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update comment error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        CommentLong old = find(id);

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("HibernateCommentLongDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete comment error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<CommentLong> listByReviewId(long reviewId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<CommentLong>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from CommentLong comment where comment.reviewId=? order by likeCnt desc");
            query.setLong(0, reviewId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentLongDAO: listByReviewId: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentLongDAO listByReviewId error");
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

            CommentLong old = (CommentLong) session.get(CommentLong.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }
            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_comment_long VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentLongDAO: like: hibernate error");
            throw new ExecuteException("HibernateCommentLongDAO like error");
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

            CommentLong old = (CommentLong) session.get(CommentLong.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_comment_long WHERE user_id='"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentLongDAO: cancel like: hibernate error");
            throw new ExecuteException("HibernateCommentLongDAO cancel like error");
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

            Query query = session.createSQLQuery("SELECT * FROM like_comment_long WHERE user_id=\'"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentLongDAO: likeExist: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentLongDAO likeExist error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }

}
