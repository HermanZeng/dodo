package dao.impl;

import dao.BookReviewDAO;
import entity.BookReviewLong;
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
import java.util.UUID;

/**
 * Created by fan on 9/10/2016.
 */
public class HibernateBookReviewLongDAO implements BookReviewDAO<BookReviewLong> {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public BookReviewLong save(BookReviewLong entity) throws ExecuteException {
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
            logger.error("BookReviewLongDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save book review long error");
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        BookReviewLong old = find(id);

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete book review long error: " + e.getMessage());
        } finally {
            session.close();
        }
    }

    @Override
    public BookReviewLong update(long id, BookReviewLong entity) throws ExecuteException, NotFoundException {
        Session session = null;
        BookReviewLong old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (BookReviewLong) session.get(BookReviewLong.class, id);
            if (old == null) {
                throw new NotFoundException("BookReviewLongDAO: update: book review long not found by id");
            }

            old.setContent(entity.getContent());
            old.setTitle(entity.getTitle());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update book review long error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public BookReviewLong find(long id) throws ExecuteException, NotFoundException {
        BookReviewLong entity = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            entity = (BookReviewLong) session.get(BookReviewLong.class, id);

            if (entity == null) {
                logger.trace("BookReviewLongDAO: book review long not found");
                throw new NotFoundException("No such book review long (by id)");
            }
        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongDAO: find: hibernate error" + e.getMessage());
        } finally {
            session.close();
        }
        return entity;
    }

    @Override
    public List<BookReviewLong> listByReviewer(String userId, int start, int count) throws ExecuteException {
        List ret = new ArrayList<BookReviewLong>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from BookReviewLong review where review.reviewer=? order by review.date desc ");
            query.setString(0, userId);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: listByReviewer: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongDAO listByReviewer error");
        } finally {
            session.close();
        }
        return ret;
    }

    @Override
    public List<BookReviewLong> listByWid(String wid, int start, int count) throws ExecuteException {
        List ret = new ArrayList<BookReviewLong>();
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from BookReviewLong bookReviewLong where bookReviewLong.wid=? order by likeCnt desc");
            query.setString(0, wid);
            query.setMaxResults(count);
            query.setFirstResult(start);
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: listByWid: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongDAO listByWid error");
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

            BookReviewLong old = (BookReviewLong) session.get(BookReviewLong.class, id);
            if (old == null) {
                throw new ExecuteException("review not found");
            }

            old.setLikeCnt(old.getLikeCnt() + 1);

            session.update(old);

            Query query = session.createSQLQuery("INSERT INTO like_bookreview_long VALUES (\'"
                    + userId
                    + "\' , \'"
                    + id
                    + "\');");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("BookReviewLongDAO: like: hibernate error");
            throw new ExecuteException("BookReviewLongDAO like error");
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

            BookReviewLong old = (BookReviewLong) session.get(BookReviewLong.class, id);

            if (old == null) {
                throw new ExecuteException("review not found");
            }

            old.setLikeCnt(old.getLikeCnt() - 1);

            session.update(old);

            Query query = session.createSQLQuery("DELETE FROM like_bookreview_long WHERE user_id='"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            query.executeUpdate();
            tx.commit();

        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            logger.error("BookReviewLongDAO: cancel like: hibernate error");
            throw new ExecuteException("BookReviewLongDAO cancel like error");
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

            Query query = session.createSQLQuery("SELECT * FROM like_bookreview_long WHERE user_id=\'"
                    + userId
                    + "\' and review_id=\'"
                    + id
                    + "\';");
            ret = query.list();
            tx.commit();

        } catch (HibernateException e) {
            logger.error("BookReviewLongDAO: likeExists: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongDAO likeExists error");
        } finally {
            session.close();
        }
        return !ret.isEmpty();
    }


}
