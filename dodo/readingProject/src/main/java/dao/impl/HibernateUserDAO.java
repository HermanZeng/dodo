package dao.impl;

import dao.UserDAO;
import entity.User;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import utilities.HibernateUtil;

import java.util.List;
import java.util.UUID;

/**
 * Created by fan on 6/28/2016.
 */
public class HibernateUserDAO implements UserDAO {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public User save(User user) throws ExecuteException, ConflictException {
        User already = null;
        try {
            already = findByEmail(user.getEmail());
        } catch (NotFoundException e) {
            Session session = null;
            try {
                user.setId(UUID.randomUUID().toString());

                SessionFactory sf = HibernateUtil.getSessionFactory();
                session = sf.openSession();
                Transaction tx = session.beginTransaction();

                session.save(user);

                tx.commit();

            } catch (HibernateException he) {
                logger.error("userDAO: save: hibernate error");
                he.printStackTrace();
                throw new ExecuteException("Save user error");
            } finally {
                session.close();
            }
            return user;
        }
        logger.trace("userDAO: save: conflict");
        throw new ConflictException("User already exists");

    }

    @Override
    public void delete(String userId) throws ExecuteException, NotFoundException {
        User old = find(userId);

        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.delete(old);

            tx.commit();
        } catch (HibernateException e) {
            logger.error("userDAO: delete: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("delete user error: " + e.getMessage());
        } finally {
            session.close();
        }

    }

    @Override
    public User update(String userId, User user) throws ExecuteException, NotFoundException {

        Session session = null;
        User old = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (User) session.get(User.class, userId); // not use userDAO.find() because it would erase user's books
            if (old == null) {
                throw new NotFoundException("userDAO: update: not found user by id");
            }

            old.setEmail(user.getEmail());
//            old.setPassword(user.getPassword());
            old.setFirstname(user.getFirstname());
            old.setLastname(user.getLastname());
            old.setRoles(user.getRoles());

            session.update(old);

            tx.commit();

        } catch (HibernateException e) {
            logger.error("userDAO: update: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("update user error");
        } finally {
            session.close();
        }
        return old;
    }

    @Override
    public List<User> findAll(int start, int count) throws ExecuteException {
        List<User> userList = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM User");
            query.setFirstResult(start);
            query.setMaxResults(count);
            userList = query.list(); // returns an empty list(not null) when there's no result
            tx.commit();

        } catch (HibernateException e) {
            logger.error("userDAO: findAll: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("List user error: ");
        } finally {
            session.close();
        }
        return userList;
    }

    @Override
    public User findByEmail(String email) throws ExecuteException, NotFoundException {
        User user = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from User user where user.email=?");
            query.setString(0, email);
            user = (User) query.uniqueResult();
            tx.commit();

            if (user == null) {
                logger.trace("userDAO: findByEmail not found");
                throw new NotFoundException("No such user (by email)");
            }
        } catch (HibernateException e) {
            logger.error("userDAO: findByEmail: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("Get user by email error");
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User find(String id) throws ExecuteException, NotFoundException {
        User user = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from User user where user.id=?");
            query.setString(0, id);
            user = (User) query.uniqueResult();
            tx.commit();

            if (user == null) {
                logger.trace("userDAO: find not found");
                throw new NotFoundException("No such user (by id)");
            }
        } catch (HibernateException e) {
            logger.error("userDAO: find: hibernate error");
            e.printStackTrace();
            throw new ExecuteException("Get user by email error: " + e.getMessage());
        } finally {
            session.close();
        }
        return user;
    }

}
