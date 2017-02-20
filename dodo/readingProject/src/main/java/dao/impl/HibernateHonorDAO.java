package dao.impl;

import dao.HonorDAO;
import entity.Honor;
import exception.dao.ExecuteException;
import org.hibernate.*;
import utilities.HibernateUtil;

import java.util.List;

/**
 * Created by heming on 9/9/2016.
 */
public class HibernateHonorDAO implements HonorDAO{
    @Override
    public Honor save(Honor honor) throws ExecuteException {
        Session session = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            session.save(honor);
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("Hibernate error: add honor failed" + he.getMessage());
        } finally {
            session.close();
        }

        return honor;
    }

    @Override
    public List<Honor> listHonor(String userId) {
        Session session = null;
        List<Honor> honorList = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            session = sf.openSession();
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM Honor honor where honor.userId=:user_id");
            query.setString("user_id", userId);
            honorList = query.list(); // returns an empty list(not null) when there's no result
            tx.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            throw new ExecuteException("List honor error: " + he.getMessage());
        } finally {
            session.close();
        }

        return honorList;
    }

}
