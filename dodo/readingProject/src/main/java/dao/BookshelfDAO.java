package dao;

import entity.Book;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by heming on 7/13/2016.
 */
public interface BookshelfDAO {
    public Book save(String userId, String bookId) throws ExecuteException, ConflictException, NotFoundException;
    public void delete(String userId, String bookId) throws ExecuteException, NotFoundException;
}
