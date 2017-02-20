package dao;

import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public interface BookReviewDAO<T>{
    T save(T entity)throws ExecuteException;

    void delete(long id)  throws ExecuteException, NotFoundException;

    T update(long id, T entity) throws ExecuteException, NotFoundException;

    T find(long id) throws ExecuteException, NotFoundException;

    List<T> listByReviewer(String userId, int start, int count)throws ExecuteException;

    List<T> listByWid(String wid, int start, int count)throws ExecuteException;

    void like(String userId, long id) throws ConflictException, ExecuteException;

    void cancelLike(String userId, long id) throws NotFoundException, ExecuteException;
}
