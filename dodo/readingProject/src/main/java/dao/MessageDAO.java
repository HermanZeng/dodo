package dao;

import entity.Message;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by heming on 9/1/2016.
 */
public interface MessageDAO {
    Message save(Message message) throws ExecuteException, IllegalInputException;

    void delete(long id) throws ExecuteException, NotFoundException;

    Message update(long id, Message message) throws ExecuteException, NotFoundException;

    Message find(long id) throws ExecuteException, NotFoundException;
    
    List<Message> findByUser(String userId) throws ExecuteException;
}
