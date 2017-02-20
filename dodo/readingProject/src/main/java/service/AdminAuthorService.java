package service;

import dao.impl.HibernateAuthorDAO;
import entity.Author;
import exception.dao.ExecuteException;

import java.util.List;


/**
 * Created by heming on 7/13/2016.
 */
public class AdminAuthorService {
    private HibernateAuthorDAO authorDAO = new HibernateAuthorDAO();

    public List<Author> findByName(String name, int start, int count) {
        List<Author> entities = null;

        try {
            entities = authorDAO.findByName(name, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException(ee.getMessage());
        }

        return entities;
    }

    public HibernateAuthorDAO getAuthorDAO() {
        return authorDAO;
    }

    public void setAuthorDAO(HibernateAuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }
}
