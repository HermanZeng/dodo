package service;

import dao.ReadingProgressDAO;
import entity.ReadingLog;
import entity.ReadingProgress;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import observer.event.BookFinishedEvent;
import observer.listener.BookFinishedListener;
import observer.listener.impl.BookFinishedAction;
import security.token.TokenManager;
import utilities.TimeUtil;

import java.util.List;

/**
 * Created by fan on 7/18/2016.
 */
public class ReadingProgressService {
    private TokenManager tokenManager;
    private ReadingProgressDAO progressDAO;

    public void setTotalPages(String token, String bookId, Integer total) throws NotFoundException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        try {

            ReadingProgress progress = progressDAO.find(userId, bookId);
            progress.setTotal(total);
            progressDAO.update(progress.getId(), progress);

        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("setTotalPages error: not found" + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            throw new ExecuteException("setTotalPages error" + e.getMessage());
        } catch (exception.dao.IllegalInputException e) {
            throw new IllegalInputException("setTotalPages error: illegal input" + e.getMessage());
        }
    }

    public void addProgress(String token, String bookId, Integer currentPage) throws IllegalInputException, NotFoundException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        BookFinishedEvent event = null;
        BookFinishedListener listener = null;

        try {

            ReadingProgress progress = progressDAO.find(userId, bookId);
            progress.setCurrent(currentPage);
            progress.setDate(TimeUtil.getCurrentDate());
            progressDAO.update(progress.getId(), progress);

            /* Add message and honor to user */
            if (progress.getCurrent().equals(progress.getTotal())) {
                event = new BookFinishedEvent();
                event.setUserId(progress.getUserId());
                event.setBookId(progress.getBookId());
                listener =new BookFinishedAction();
                listener.doAction(event);
            }

        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("addProgress error: not found" + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            throw new ExecuteException("addProgress error" + e.getMessage());
        } catch (exception.dao.IllegalInputException e) {
            throw new IllegalInputException("addProgress error: illegal input" + e.getMessage());
        }
    }

    public List<ReadingLog> findReadingLog(String token, String bookId, int start, int count) throws NotFoundException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        List<ReadingLog> readingLogs = null;

        try {

            readingLogs = progressDAO.findLog(userId, bookId, start, count);

        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("findReadingLog error: not found" + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            throw new ExecuteException("findReadingLog error" + e.getMessage());
        }

        return readingLogs;
    }

    public List<ReadingProgress> findAllProgress(String token) throws ExecuteException {
        String userId = tokenManager.getUserId(token);

        List<ReadingProgress> list = null;

        try {
            list = progressDAO.findAll(userId);
        } catch (exception.dao.ExecuteException e) {
            throw new ExecuteException("findAllProgress error" + e.getMessage());
        }

        return list;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public ReadingProgressDAO getProgressDAO() {
        return progressDAO;
    }

    public void setProgressDAO(ReadingProgressDAO progressDAO) {
        this.progressDAO = progressDAO;
    }
}
