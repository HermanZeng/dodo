package dao.impl;

import com.alibaba.fastjson.JSON;
import dao.BookReviewDAO;
import entity.BookReviewShort;
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
 * Created by fan on 9/10/2016.
 */
public class HibernateBookReviewShortDAO implements BookReviewDAO<BookReviewShort> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public BookReviewShort save(BookReviewShort entity) throws ExecuteException {
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
            logger.error("BookReviewShortDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save book review short error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        BookReviewShort old = find(id);

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("BookReviewShortDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete book review short error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public BookReviewShort update(long id, BookReviewShort entity) throws ExecuteException, NotFoundException {
        Session session = null;
        BookReviewShort old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (BookReviewShort) session.get(BookReviewShort.class, id);
            if (old == null) {
                throw new NotFoundException("BookReviewShortDAO: update: book review short not found by id");
            }

            old.setContent(entity.getContent());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewShortDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update book review short error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public BookReviewShort find(long id) throws ExecuteException, NotFoundException {
        BookReviewShort entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (BookReviewShort) session.get(BookReviewShort.class, id);

            if (entity == null) {
                logger.trace("BookReviewShortDAO: book review short not found");
                throw new NotFoundException("No such book review short (by id)");
            }
        } catch (HibernateException e) {
            logger.error("BookReviewShortDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public List<BookReviewShort> listByReviewer(String userId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<BookReviewShort>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from BookReviewShort review where review.reviewer=? order by review.date desc");
            query.setString(0, userId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret =  query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewShortDAO: listByReviewer: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortDAO listByReviewer error");
        } finally {
            session.close();
        }
        return ret;
    }

    @Override
    public List<BookReviewShort> listByWid(String wid, int start, int count) throws ExecuteException {
        List ret = new ArrayList<BookReviewShort>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from BookReviewShort review where review.wid=? order by review.likeCnt desc ");
            query.setString(0, wid);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret =  query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewShortDAO: listByWid: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortDAO listByWid error");
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

            BookReviewShort old = (BookReviewShort) session.get(BookReviewShort.class, id);
            if (old == null) {
                throw new ExecuteException("review not found");
            }

            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_bookreview_short VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("BookRevieShortgDAO: like: hibernate error");
            throw new ExecuteException("BookRevieShortgDAO like error");
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

            BookReviewShort old = (BookReviewShort) session.get(BookReviewShort.class, id);
            if (old == null) {
                throw new ExecuteException("review not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_bookreview_short WHERE user_id='"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("BookRevieShortgDAO: cancel like: hibernate error");
            throw new ExecuteException("BookRevieShortgDAO cancel like error");
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

            Query query = session.createSQLQuery("SELECT * FROM like_bookreview_short WHERE user_id=\'"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookRevieShortgDAO: listByWid: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookRevieShortgDAO listByWid error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }

}
