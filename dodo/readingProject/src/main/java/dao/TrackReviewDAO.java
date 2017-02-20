package dao;

import entity.TrackReview;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public interface TrackReviewDAO{
    TrackReview save(TrackReview entity)throws ExecuteException;

    TrackReview find(long id) throws ExecuteException, NotFoundException;

    TrackReview update(long id, TrackReview entity) throws ExecuteException, NotFoundException;

    void delete(long id)  throws ExecuteException, NotFoundException;

    List<TrackReview> listByReviewer(String userId, int start, int count)throws ExecuteException;

    List<TrackReview> listByTrackId(String trackId, int start, int count)throws ExecuteException;

    void like(String userId, long id) throws ConflictException, ExecuteException;

    void cancelLike(String userId, long id) throws NotFoundException, ExecuteException;
}
