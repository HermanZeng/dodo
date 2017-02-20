package dao.impl;

import entity.PullRequest;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;
import utilities.TimeUtil;

/**
 * Created by fan on 7/20/2016.
 */
public class HibernatePullRequestDAO {
    private static final Logger logger = LogManager.getLogger();

    public void save(PullRequest pullRequest) throws ExecuteException {
        pullRequest.setDate(TimeUtil.getCurrentDate());
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.save(pullRequest);

            tx.commit();
        } catch (HibernateException he) {
            logger.error("pullRequestDAO: save: hibernate error");
            he.printStackTrace();
            throw new ExecuteException("Save pull request error");
        } finally {
            session.close();
        }
    }

    public PullRequest find(Long id) throws NotFoundException, ExecuteException {
        PullRequest pullRequest = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            pullRequest = (PullRequest) session.get(PullRequest.class, id);
            tx.commit();

            if (pullRequest == null) {
                throw new NotFoundException("No such pull request (by id)");
            }
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("pullRequestDAO: Find pull request by id error");
        } finally {
            session.close();
        }
        return pullRequest;
    }

    public void delete(Long id) throws NotFoundException, ExecuteException {
        PullRequest pullRequest = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            pullRequest = (PullRequest) session.get(PullRequest.class, id);

            if (pullRequest == null) {
                throw new NotFoundException("No such pull request (by id)");
            }

            session.delete(pullRequest);
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("pullRequestDAO: delete pull request by id error");
        } finally {
            session.close();
        }
    }
}
