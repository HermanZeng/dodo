package dao;

import entity.ReadingLog;
import entity.ReadingProgress;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by fan on 7/19/2016.
 */
public interface ReadingProgressDAO {

    ReadingProgress save(ReadingProgress progress) throws ExecuteException;

    ReadingProgress update(long progressId, ReadingProgress progress) throws IllegalInputException, NotFoundException, ExecuteException;

    List<ReadingProgress> findAll(String userId) throws ExecuteException;

    List<ReadingLog> findLog(String userId, String bookId, int start, int count) throws NotFoundException, ExecuteException;

    ReadingProgress find(String userId, String bookId) throws NotFoundException, ExecuteException;

}
