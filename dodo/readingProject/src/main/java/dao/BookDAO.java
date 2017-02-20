package dao;

import entity.Book;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by heming on 7/11/2016.
 */
public interface BookDAO extends DAO<Book>{
    List<Book> findAll(int start, int count) throws ExecuteException;

    Book findByIsbn(String isbn) throws ExecuteException, NotFoundException;

    List<Book> searchByTitle(String title, int start, int count) throws ExecuteException;

    List<Book> searchByIsbn(String isbn, int start, int count) throws ExecuteException;

    List<String> searchByCategory(int category, int start, int count) throws ExecuteException;
}
