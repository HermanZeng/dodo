package service;

import dao.impl.HibernateTranslatorDAO;
import entity.Translator;
import exception.dao.ExecuteException;

import java.util.List;


/**
 * Created by heming on 7/13/2016.
 */
public class AdminTranslatorService {
    private HibernateTranslatorDAO translatorDAO = new HibernateTranslatorDAO();

    public List<Translator> findByName(String name, int start, int count) {
        List<Translator> entities = null;

        try {
            entities = translatorDAO.findByName(name, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException(ee.getMessage());
        }

        return entities;
    }

    public HibernateTranslatorDAO getTranslatorDAO() {
        return translatorDAO;
    }

    public void setTranslatorDAO(HibernateTranslatorDAO translatorDAO) {
        this.translatorDAO = translatorDAO;
    }
}
