package service;

import dao.BookReviewDAO;
import dao.CommentDAO;
import dao.TrackReviewDAO;
import dao.UserDAO;
import dao.impl.HibernateTrackReviewDAO;
import dao.impl.HibernateUserDAO;
import entity.*;
import entity.CommentTrack;
import exception.service.*;
import security.token.TokenManager;
import utilities.SpringIocUtil;

import java.util.List;

/**
 * Created by fan on 9/10/2016.
 */
public class CommentTrackService {
    private TokenManager tokenManager;

    private CommentDAO<CommentTrack> commentDAO;

    public CommentTrack add(String token, CommentTrack comment) throws IllegalInputException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        if (!validCreateComment(comment)) {
            throw new IllegalInputException("invalid comment");
        }

        comment.setUserId(userId);

        try {
            comment = commentDAO.save(comment);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService add error: " + e.getMessage());
        }
        sendMessage(userId, comment);
        return comment;
    }

    public CommentTrack get(Long commentId) throws NotFoundException, ExecuteException {

        CommentTrack ret = null;

        try {
            ret = commentDAO.find(commentId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService get error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("comment not found: " + e.getMessage());

        }

        return ret;

    }

    public CommentTrack update(String token, Long commentId, CommentTrack comment) throws IllegalInputException, NotFoundException, ForbiddenException, ExecuteException {

        if (!validUpdateComment(comment)) {
            throw new IllegalInputException("invalid comment");
        }

        CommentTrack old = null;

        try {
            old = commentDAO.find(commentId);
            String userId = tokenManager.getUserId(token);

            if (!old.getUserId().equals(userId)) {
                throw new ForbiddenException("cannot edit other's comment");
            }

            old = commentDAO.update(commentId, comment);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService update error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("comment not found: " + e.getMessage());
        }
        return old;

    }

    public void delete(String token, Long commentId) throws NotFoundException, ForbiddenException, ExecuteException {
        try {
            CommentTrack old = commentDAO.find(commentId);

            String userId = tokenManager.getUserId(token);
            Boolean isAdmin = tokenManager.isAdmin(token);
            if ((!old.getUserId().equals(userId)) && !isAdmin) {
                throw new ForbiddenException("cannot edit other's comment");
            }

            commentDAO.delete(commentId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService delete error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("comment not found: " + e.getMessage());
        }

    }

    public List<CommentTrack> listByReviewId(long reviewId, int start, int count) throws ExecuteException {
        List<CommentTrack> ret;
        try {
            ret = commentDAO.listByReviewId(reviewId, start, count);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService list by reviewId error: " + e.getMessage());
        }

        return ret;
    }

    public void like(String token, Long commentId) throws ConflictException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        try {
            commentDAO.like(userId, commentId);
        } catch (exception.dao.ConflictException e) {
            throw new ConflictException("cannot like twice: " + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService like error: " + e.getMessage());
        }
    }

    public void cancelLike(String token, Long commentId) throws NotFoundException, ExecuteException {
        String userId = tokenManager.getUserId(token);

        try {
            commentDAO.cancelLike(userId, commentId);
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("previous 'like' not found: " + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("CommentService cancel like error: " + e.getMessage());
        }
    }

    private boolean validCreateComment(CommentTrack comment) {
        return !(comment.getReviewId() == null ||
                comment.getContent() == null || comment.getContent().equals(""));
    }

    private boolean validUpdateComment(CommentTrack comment) {
        return !(comment.getContent() == null || comment.getContent().equals(""));
    }

    private void sendMessage(String userId, CommentTrack comment) {
        UserDAO userDAO = new HibernateUserDAO();
        TrackReviewDAO reviewDAO = new HibernateTrackReviewDAO();
        MessageService messageService = SpringIocUtil.getBean("messageService", MessageService.class);

        try {
            String reviewerId = reviewDAO.find(comment.getReviewId()).getReviewer();

            User commenter = userDAO.find(userId);

            String msg = commenter.getFirstname() + " commented your track review!";
            messageService.addMessage(reviewerId, msg, Global.CommentOfTrack, comment.getId().toString());

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public CommentDAO getCommentDAO() {
        return commentDAO;
    }

    public void setCommentDAO(CommentDAO commentDAO) {
        this.commentDAO = commentDAO;
    }
}
