package dao;

import entity.Track;
import exception.dao.ExecuteException;

import java.util.List;

/**
 * Created by heming on 7/18/2016.
 */
public interface TrackDAO extends DAO<Track> {
    List<Track> findAll(int start, int count) throws ExecuteException;

    List<Track> search(String titleQuery, int start, int count) throws ExecuteException;

}
