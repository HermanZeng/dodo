package dao.impl;

import dao.ReadingProgressDAO;
import entity.ReadingLog;
import entity.ReadingProgress;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import exception.dao.NotFoundException;
import observer.event.BookFinishedEvent;
import observer.listener.BookFinishedListener;
import observer.listener.impl.BookFinishedAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;
import utilities.TimeUtil;

import java.util.List;

/**
 * Created by fan on 7/18/2016.
 */
public class HibernateReadingProgressDAO implements ReadingProgressDAO {
    private static final Logger logger = LogManager.getLogger();

    public ReadingProgress save(ReadingProgress progress) throws ExecuteException {
        ReadingProgress old = null;
        try {
            old = find(progress.getUserId(), progress.getBookId());
        } catch (NotFoundException ne) {
            progress.setDate(TimeUtil.getCurrentDate());
            progress.setCurrent(0);

            ReadingLog readingLog = new ReadingLog();
            readingLog.setCurrent(progress.getCurrent());
            readingLog.setDate(progress.getDate());
            readingLog.setBookId(progress.getBookId());
            readingLog.setUserId(progress.getUserId());

            Session session = null;
            try {
                SessionFactory sf = HibernateUtil.getSessionFactory();
                session = sf.openSession();
                Transaction tx = session.beginTransaction();

                session.save(progress);
                session.save(readingLog);

                tx.commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                logger.error("HibernateReadingProgressDAO: save: hibernate error");
                throw new ExecuteException("Save reading progress error");
            } finally {
                session.close();
            }
            return progress;
        }
        return old;
    }

    public ReadingProgress update(long progressId, ReadingProgress progress) throws IllegalInputException, NotFoundException, ExecuteException {
        ReadingProgress old = null;
        Session session = null;


        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (ReadingProgress) session.get(ReadingProgress.class, progressId);
            if (old == null) {
                throw new NotFoundException("HibernateReadingProgressDAO: update: not found reading progress by id");
            }
            if (progress.getCurrent() < old.getCurrent()) {
                throw new IllegalInputException("HibernateReadingProgressDAO: update: invalid current");
            }
            if (progress.getTotal() < old.getCurrent()) {
                throw new IllegalInputException("HibernateReadingProgressDAO: update: invalid total");
            }
            if (progress.getCurrent() > old.getTotal()) {
                throw new IllegalInputException("HibernateReadingProgressDAO: update: invalid current");
            }

            old.setTotal(progress.getTotal());
            old.setCurrent(progress.getCurrent());
            old.setDate(progress.getDate());

            ReadingLog readingLogNew = new ReadingLog();
            readingLogNew.setUserId(old.getUserId());
            readingLogNew.setBookId(old.getBookId());
            readingLogNew.setDate(old.getDate());
            readingLogNew.setCurrent(old.getCurrent());

            session.update(old);
            session.save(readingLogNew);
            tx.commit();


        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: update user error");
        } finally {
            session.close();
        }



        return old;
    }

    public List<ReadingProgress> findAll(String userId) throws ExecuteException {

        List<ReadingProgress> lists = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM ReadingProgress r where r.userId=:user_id order by r.date desc");
            query.setString("user_id", userId);
            lists = query.list(); // returns an empty list(not null) when there's no result
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateReadingProgressDAO: findAll: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("List reading progress error: ");
        } finally {
            session.close();
        }

        return lists;
    }

    public List<ReadingLog> findLog(String userId, String bookId, int start, int count) throws NotFoundException, ExecuteException {

        List<ReadingLog> lists = null;

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM ReadingLog r where r.userId=:user_id and r.bookId=:book_id order by r.date desc ");
            query.setString("user_id", userId);
            query.setString("book_id", bookId);
            query.setFirstResult(start);
            query.setMaxResults(count);
            lists = query.list(); // returns an empty list(not null) when there's no result
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateReadingProgressDAO: findLog: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("find log error: ");
        } finally {
            session.close();
        }

        if (lists.isEmpty()) {
            logger.debug("HibernateReadingProgressDAO: findLog: reading log not found");
            throw new NotFoundException("HibernateReadingProgressDAO: findLog: reading log not found");
        }

        return lists;

    }

    public ReadingProgress find(String userId, String bookId) throws NotFoundException, ExecuteException {

        ReadingProgress progress = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM ReadingProgress r where r.userId=:user_id and r.bookId=:book_id");
            query.setString("user_id", userId);
            query.setString("book_id", bookId);
            progress = (ReadingProgress) query.uniqueResult(); // returns an empty list(not null) when there's no result
            tx.commit();

        } catch (HibernateException e) {
            logger.error("HibernateReadingProgressDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("find reading progress error: ");
        } finally {
            session.close();
        }

        if (progress == null) {
            logger.debug("HibernateReadingProgressDAO: find: reading progress not found");
            throw new NotFoundException("HibernateReadingProgressDAO: find: reading progress not found");
        }

        return progress;
    }
}
