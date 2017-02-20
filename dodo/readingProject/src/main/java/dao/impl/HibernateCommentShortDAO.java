package dao.impl;

import dao.CommentDAO;
import entity.CommentShort;
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
public class HibernateCommentShortDAO implements CommentDAO<CommentShort> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public CommentShort save(CommentShort entity) throws ExecuteException {
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
            logger.error("HibernateCommentShortDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save comment error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentShort find(long id) throws ExecuteException, NotFoundException {
        CommentShort entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (CommentShort) session.get(CommentShort.class, id);

            if (entity == null) {
                logger.trace("HibernateCommentShortDAO: comment not found");
                throw new NotFoundException("No such comment (by id)");
            }
        } catch (HibernateException e) {
            logger.error("HibernateCommentShortDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentShortDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public CommentShort update(long id, CommentShort entity) throws ExecuteException, NotFoundException {
        Session session = null;
        CommentShort old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (CommentShort) session.get(CommentShort.class, id);
            if (old == null) {
                throw new NotFoundException("HibernateCommentShortDAO: update: comment not found by id");
            }

            old.setContent(entity.getContent());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentShortDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update comment error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        CommentShort old = find(id);

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("HibernateCommentShortDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete comment error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public List<CommentShort> listByReviewId(long reviewId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<CommentShort>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from CommentShort comment where comment.reviewId=? order by likeCnt desc");
            query.setLong(0, reviewId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentShortDAO: listByReviewId: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentShortDAO listByReviewId error");
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

            CommentShort old = (CommentShort) session.get(CommentShort.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }
            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_comment_short VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentShortDAO: like: hibernate error");
            throw new ExecuteException("HibernateCommentShortDAO like error");
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

            CommentShort old = (CommentShort) session.get(CommentShort.class, id);
            if (old == null) {
                throw new ExecuteException("comment not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_comment_short WHERE user_id='"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("HibernateCommentShortDAO: cancel like: hibernate error");
            throw new ExecuteException("HibernateCommentShortDAO cancel like error");
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

            Query query = session.createSQLQuery("SELECT * FROM like_comment_short WHERE user_id=\'"
                    + userId
                    + "\' and comment_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateCommentShortDAO: likeExist: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("HibernateCommentShortDAO likeExist error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }

}
