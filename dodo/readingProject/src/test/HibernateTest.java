import com.alibaba.fastjson.JSON;
import dao.MessageDAO;
import dao.impl.HibernateMessageDAO;
import entity.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utilities.HibernateUtil;
import utilities.TimeUtil;

import java.util.List;

/**
 * Created by heming on 6/27/2016.
 */
public class HibernateTest {
    public static void main(String[] args) {
        //SessionFactory sf = new Configuration().configure().buildSessionFactory();
        /*SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Message message where message.id= :id");
        Long num = new Long(1);
        query.setParameter("id", num);
        Message message = (Message) query.uniqueResult();

        /*Query query = session.createQuery("from Role where id = :id");
        query.setInteger("id", 1);
        Role role = (Role) query.uniqueResult();*/

        //tx.commit();

        /*for (Author a :
                book.getAuthors()) {
            a.setBooks(null);
        }
        for (Translator t :
                book.getTranslators()) {
            t.setBooks(null);
        }*/
        /*String json = JSON.toJSONString(message);
        System.out.println(json);

        session.close();*/


        String json = null;
        MessageDAO messageDAO = new HibernateMessageDAO();
        List<Message> message1 =  messageDAO.findByUser("2");
        json = JSON.toJSONString(message1);
        System.out.println(json);

        SessionFactory sf2 = HibernateUtil.getSessionFactory();
        Session session2 = sf2.openSession();
        Transaction tx2 = session2.beginTransaction();
        Message message2 = new Message();
        //message2.setId(new Long(1));
        message2.setUserId("8xkQuQPHZornsprphns0am52W14=");
        message2.setRead(Global.NotRead);
        message2.setType(Global.Honor);
        message2.setMessage("Test Msg");
        message2.setDate(TimeUtil.getCurrentDate());
        System.out.println(JSON.toJSONString(message2));
        session2.save(message2);
        tx2.commit();
        session2.close();
        //System.out.println(role.getDescription());


    }
}
