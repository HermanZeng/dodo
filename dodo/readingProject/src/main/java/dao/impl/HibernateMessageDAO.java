package dao.impl;

import com.alibaba.fastjson.JSON;
import dao.MessageDAO;
import entity.Global;
import entity.Message;
import entity.User;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import exception.dao.NotFoundException;
import org.hibernate.*;
import utilities.HibernateUtil;
import utilities.TimeUtil;

import java.util.List;
import java.util.UUID;

/**
 * Created by heming on 9/1/2016.
 */
public class HibernateMessageDAO implements MessageDAO {
    @Override
    public List<Message> findByUser(String userId) throws ExecuteException{
        List<Message> messageList = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM Message message where message.userId=:user_id order by message.date desc");
            query.setString("user_id", userId);
            messageList = query.list(); // returns an empty list(not null) when there's no result
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("List messages error: " + he.getMessage());
        } finally {
            session.close();
        }
        return messageList;
    }

    @Override
    public Message save(Message message) throws ExecuteException, IllegalInputException {
        User user = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            if (message.getRead() == Global.Read) {
                throw new IllegalInputException("Message cannot be read");
            }


            message.setDate(TimeUtil.getCurrentDate());
            session.save(message);
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: add a new message failed" + he.getMessage());
        } finally {
            session.close();
        }

        return message;
    }

    @Override
    public void delete(long id) throws ExecuteException, NotFoundException {
        Message message = find(id);
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.delete(message);

            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: delete message");
        } finally {
            session.close();
        }
    }

    @Override
    public Message update(long id, Message message) throws ExecuteException, NotFoundException {
        Message old = null;
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            old = (Message) session.get(Message.class, id);

            old.setUserId(message.getUserId());
            old.setMessage(message.getMessage());
            old.setRead(message.getRead());
            old.setDate(TimeUtil.getCurrentDate());
            old.setType(message.getType());
            old.setReqId(message.getReqId());

            session.update(old);
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: update user error");
        } finally {
            session.close();
        }

        return old;
    }

    @Override
    public Message find(long id) throws ExecuteException, NotFoundException {
        Message message = null;
        Session session = null;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("from Message message where message.id=?");
            query.setParameter(0, id);
            message = (Message) query.uniqueResult();
            tx.commit();

            if (message == null) {
                throw new NotFoundException("No such message (by id)");
            }
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("HibernateMessageDAO: Find message by id error " + he.getMessage());
        } finally {
            session.close();
        }
        return message;
    }
}
