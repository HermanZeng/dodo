package service;

import dao.BookReviewDAO;
import entity.BookReviewShort;
import exception.dao.*;
import exception.service.*;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import security.token.TokenManager;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public class BookReviewShortService {
    private TokenManager tokenManager;

    private BookReviewDAO<BookReviewShort> reviewDAO;

    public BookReviewShort add(String token, BookReviewShort review) throws IllegalInputException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validCreateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        review.setReviewer(userId);

        try {
            review = reviewDAO.save(review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService add short book review error: " + e.getMessage());
        }
        return review;
    }

    public BookReviewShort get(Long reviewId) throws NotFoundException, ExecuteException {

        BookReviewShort ret = null;

        try {
            ret = reviewDAO.find(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService get short book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());

        }

        return ret;

    }

    public BookReviewShort update(String token, Long reviewId, BookReviewShort review) throws IllegalInputException, NotFoundException, ForbiddenException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validUpdateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        BookReviewShort old = null;

        try {
            old = reviewDAO.find(reviewId);

            if (!old.getReviewer().equals(userId)) {
                throw new ForbiddenException("cannot edit other's review");
            }

            old = reviewDAO.update(reviewId, review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService update short book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }
        return old;

    }

    public void delete(String token, Long reviewId) throws NotFoundException, ForbiddenException, ExecuteException {
        try {
            BookReviewShort old = reviewDAO.find(reviewId);
            String userId = tokenManager.getUserId(token);
            Boolean isAdmin = tokenManager.isAdmin(token);

            if ((!old.getReviewer().equals(userId)) && !isAdmin) {
                throw new ForbiddenException("cannot edit other's review");
            }

            reviewDAO.delete(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService delete short book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }

    }

    public List<BookReviewShort> listByReviewer(String userId, int start, int count) throws ExecuteException {

        List<BookReviewShort> ret;
        try {
            ret = reviewDAO.listByReviewer(userId, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService list short book review by reviewer error: " + e.getMessage());
        }

        return ret;
    }

    public List<BookReviewShort> listByWid(String wid, int start, int count) throws ExecuteException {
        List<BookReviewShort> ret;
        try {
            ret = reviewDAO.listByWid(wid, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewShortService list short book review by wid error: " + e.getMessage());
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
            throw new ExecuteException("BookReviewShortService like short book review error: " + e.getMessage());
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
            throw new ExecuteException("BookReviewShortService cancel like short book review error: " + e.getMessage());
        }
    }

    private boolean validCreateReview(BookReviewShort review) {
        return !(review.getWid() == null || review.getWid().equals("") ||
                review.getContent() == null || review.getContent().equals("") || review.getContent().length() > 140);
    }

    private boolean validUpdateReview(BookReviewShort review) {
        return !(review.getContent() == null || review.getContent().equals("") || review.getContent().length() > 140);
    }

    public BookReviewDAO<BookReviewShort> getReviewDAO() {
        return reviewDAO;
    }

    public void setReviewDAO(BookReviewDAO<BookReviewShort> reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

}
