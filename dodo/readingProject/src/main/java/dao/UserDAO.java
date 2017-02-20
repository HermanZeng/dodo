package dao;

import entity.User;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by fan on 6/28/2016.
 */
public interface UserDAO extends DAO<User> {

    List<User> findAll(int start, int count) throws ExecuteException;

    User findByEmail(String email) throws ExecuteException, NotFoundException;

}
