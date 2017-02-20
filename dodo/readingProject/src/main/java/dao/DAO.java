package dao;

import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

/**
 * Created by fan on 6/29/2016.
 */
public interface DAO<T> {

    T save(T entity) throws ExecuteException, ConflictException;

    void delete(String id) throws ExecuteException, NotFoundException;

    T update(String id, T entity) throws ExecuteException, NotFoundException;

    T find(String  id) throws ExecuteException, NotFoundException;

}
