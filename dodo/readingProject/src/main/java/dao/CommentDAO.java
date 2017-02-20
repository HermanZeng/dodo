package dao;

import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public interface CommentDAO<T> {
    T save(T entity)throws ExecuteException;

    T find(long id) throws ExecuteException, NotFoundException;

    T update(long id, T entity) throws ExecuteException, NotFoundException;

    void delete(long id)  throws ExecuteException, NotFoundException;

    List<T> listByReviewId(long reviewId, int start, int count)throws ExecuteException;

    void like(String userId, long id) throws ConflictException, ExecuteException;

    void cancelLike(String userId, long id) throws NotFoundException, ExecuteException;
}
