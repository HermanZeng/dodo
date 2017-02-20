package service;

import dao.TrackDAO;
import dao.TrackReviewDAO;
import dao.UserDAO;
import dao.impl.HibernateUserDAO;
import dao.impl.MorphiaTrackDAO;
import entity.CommentLong;
import entity.Global;
import entity.TrackReview;
import entity.User;
import exception.dao.*;
import exception.service.*;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import security.token.TokenManager;
import utilities.SpringIocUtil;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public class TrackReviewService {
    private TokenManager tokenManager;

    private TrackReviewDAO reviewDAO;

    private void sendMessage(String userId, TrackReview review) {
        UserDAO userDAO = new HibernateUserDAO();
        TrackDAO trackDAO = new MorphiaTrackDAO();
        MessageService messageService = SpringIocUtil.getBean("messageService", MessageService.class);

        try {
            String trackUserId = trackDAO.find(review.getTrackId()).getModifierId();

            User reviewer = userDAO.find(userId);

            String msg = reviewer.getFirstname() + " wrote a review of your track!";
            messageService.addMessage(trackUserId, msg, Global.ReviewOfTrack, review.getTrackId());
            System.out.println(msg);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public TrackReview add(String token, TrackReview review) throws IllegalInputException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validCreateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        review.setReviewer(userId);

        try {
            review = reviewDAO.save(review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService add error: " + e.getMessage());
        }
        sendMessage(userId, review  );
        return review;
    }

    public TrackReview get(Long reviewId) throws NotFoundException, ExecuteException {

        TrackReview ret = null;

        try {
            ret = reviewDAO.find(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService get error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());

        }

        return ret;

    }

    public TrackReview update(String token, Long reviewId, TrackReview review) throws IllegalInputException, NotFoundException, ForbiddenException, ExecuteException {

        if (!validUpdateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        TrackReview old = null;

        try {
            old = reviewDAO.find(reviewId);
            String userId = tokenManager.getUserId(token);

            if (!old.getReviewer().equals(userId)) {
                throw new ForbiddenException("cannot edit other's review");
            }

            old = reviewDAO.update(reviewId, review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService update error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }
        return old;

    }

    public void delete(String token, Long reviewId) throws NotFoundException, ForbiddenException, ExecuteException {
        try {
            TrackReview old = reviewDAO.find(reviewId);

            String userId = tokenManager.getUserId(token);
            Boolean isAdmin = tokenManager.isAdmin(token);
            if ((!old.getReviewer().equals(userId)) && !isAdmin) {
                throw new ForbiddenException("cannot edit other's review");
            }

            reviewDAO.delete(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService delete error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }

    }

    public List<TrackReview> listByReviewer(String userId, int start, int count) throws ExecuteException {

        List<TrackReview> ret;
        try {
            ret = reviewDAO.listByReviewer(userId, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService list by reviewer error: " + e.getMessage());
        }

        return ret;
    }

    public List<TrackReview> listByTrackId(String trackId, int start, int count) throws ExecuteException {
        List<TrackReview> ret;
        try {
            ret = reviewDAO.listByTrackId(trackId, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService list by trackId error: " + e.getMessage());
        }

        return ret;
    }

    public void like(String token, Long reviewId) throws ConflictException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        try {
            reviewDAO.like(userId, reviewId);
        } catch (exception.dao.ConflictException e) {
            throw new ConflictException("cannot like twice: " + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService like error: " + e.getMessage());
        }
    }

    public void cancelLike(String token, Long reviewId) throws NotFoundException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        try {
            reviewDAO.cancelLike(userId, reviewId);
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("previous \'like\' not found: " + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("TrackReviewService cancel like error: " + e.getMessage());
        }
    }

    private boolean validCreateReview(TrackReview review) {
        return !(review.getTrackId() == null || review.getTrackId().equals("") ||
                review.getContent() == null || review.getContent().equals(""));
    }

    private boolean validUpdateReview(TrackReview review) {
        return !(review.getContent() == null || review.getContent().equals(""));
    }

    public TrackReviewDAO getReviewDAO() {
        return reviewDAO;
    }

    public void setReviewDAO(TrackReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

}
