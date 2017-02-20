package service;

import com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dao.BookReviewDAO;
import entity.BookReviewLong;
import exception.dao.*;
import exception.service.*;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import security.token.TokenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public class BookReviewLongService {
    private TokenManager tokenManager;

    private BookReviewDAO<BookReviewLong> reviewDAO;

    public BookReviewLong add(String token, BookReviewLong review) throws IllegalInputException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validCreateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        review.setReviewer(userId);

        try {
            review = reviewDAO.save(review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService add long book review error: " + e.getMessage());
        }
        return review;
    }

    public BookReviewLong get(Long reviewId) throws NotFoundException, ExecuteException {

        BookReviewLong ret = null;

        try {
            ret = reviewDAO.find(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService get long book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());

        }

        return ret;

    }

    public BookReviewLong update(String token, Long reviewId, BookReviewLong review) throws IllegalInputException, NotFoundException, ForbiddenException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validUpdateReview(review)) {
            throw new IllegalInputException("invalid review");
        }

        BookReviewLong old = null;

        try {
            old = reviewDAO.find(reviewId);

            if (!old.getReviewer().equals(userId)) {
                throw new ForbiddenException("cannot edit other's review");
            }

            old = reviewDAO.update(reviewId, review);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService update long book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }
        return old;

    }

    public void delete(String token, Long reviewId) throws NotFoundException, ForbiddenException, ExecuteException {
        try {
            BookReviewLong old = reviewDAO.find(reviewId);
            String userId = tokenManager.getUserId(token);
            Boolean isAdmin = tokenManager.isAdmin(token);

            if ((!old.getReviewer().equals(userId)) && !isAdmin) {
                throw new ForbiddenException("cannot edit other's review");
            }

            reviewDAO.delete(reviewId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService delete long book review error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("review not found: " + e.getMessage());
        }

    }

    public List<BookReviewLong> listByReviewer(String userId, int start, int count) throws ExecuteException {

        List<BookReviewLong> ret;
        try {
            ret = reviewDAO.listByReviewer(userId, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService list long book review by reviewer error: " + e.getMessage());
        }

        return ret;
    }

    public List<BookReviewLong> listByWid(String wid, int start, int count) throws ExecuteException {
        List<BookReviewLong> ret;
        try {
            ret = reviewDAO.listByWid(wid, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("BookReviewLongService list long book review by wid error: " + e.getMessage());
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
            throw new ExecuteException("BookReviewLongService like long book review error: " + e.getMessage());
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
            throw new ExecuteException("BookReviewLongService cancel like long book review error: " + e.getMessage());
        }
    }

    private boolean validCreateReview(BookReviewLong review) {
        return !(review.getWid() == null || review.getWid().equals("") ||
                review.getTitle() == null || review.getTitle().equals("") ||
                review.getContent() == null || review.getContent().equals(""));
    }

    private boolean validUpdateReview(BookReviewLong review) {
        return !(review.getTitle() == null || review.getTitle().equals("") ||
                review.getContent() == null || review.getContent().equals(""));
    }


    public BookReviewDAO<BookReviewLong> getReviewDAO() {
        return reviewDAO;
    }

    public void setReviewDAO(BookReviewDAO<BookReviewLong> reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
}
