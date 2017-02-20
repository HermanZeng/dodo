package api;

import com.alibaba.fastjson.JSON;
import entity.CommentTrack;
import exception.http.BadRequestException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.CommentLongService;
import service.CommentShortService;
import service.CommentTrackService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 7/19/2016.
 */
@RestController
@RequestMapping(value = "/comment/track_review")
public class CommentTrackResource {
    private static final Logger logger = LogManager.getLogger();
    private static final CommentTrackService commentService = SpringIocUtil.getBean("commentTrackService", CommentTrackService.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@RequestBody String req) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }
        CommentTrack reqComment = JSON.parseObject(req, CommentTrack.class);
        CommentTrack comment = null;
        try {
            comment = commentService.add(token, reqComment);
        } catch (IllegalInputException e) {
            logger.debug("illegal request body: " + e.getMessage());
            throw new BadRequestException("illegal request body: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("create comment error: " + e.getMessage());
        }

        return JSON.toJSONString(comment);
    }

    @RequestMapping(value = "/{comment_id}", method = RequestMethod.GET)
    public String show(@PathVariable Long comment_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        CommentTrack comment = null;
        try {
            comment = commentService.get(comment_id);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("show comment error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("comment not found when show: " + e.getMessage());
            throw new exception.http.NotFoundException("comment not found when show:  " + e.getMessage());
        }

        return JSON.toJSONString(comment);

    }

    @RequestMapping(value = "/{comment_id}", method = RequestMethod.PUT)
    public String edit(@PathVariable Long comment_id, @RequestBody  String req) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }
        CommentTrack reqComment = JSON.parseObject(req, CommentTrack.class);
        CommentTrack comment;

        try {
            comment = commentService.update(token, comment_id, reqComment);
        } catch (IllegalInputException e) {
            logger.debug("illegal request body: " + e.getMessage());
            throw new BadRequestException("illegal request body: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("comment not found when update: " + e.getMessage());
            throw new exception.http.NotFoundException("comment not found when update:  " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("cannot edit others comment: " + e.getMessage());
            throw new exception.http.ForbiddenException("cannot edit others comment: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("edit comment error: " + e.getMessage());
        }

        return JSON.toJSONString(comment);
    }

    @RequestMapping(value = "/{comment_id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long comment_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            commentService.delete(token, comment_id);
        } catch (NotFoundException e) {
            logger.debug("comment not found when delete: " + e.getMessage());
            throw new exception.http.NotFoundException("comment not found when delete:  " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("cannot delete others comment: " + e.getMessage());
            throw new exception.http.ForbiddenException("cannot delete others comment: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("delete comment error: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/review/{review_id}", method = RequestMethod.GET)
    public String listByReviewId(@PathVariable Long review_id) {

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");

        int start = 0;
        int count = 5;
        try {
            if (startParam != null && !startParam.equals("")) {
                start = Integer.valueOf(startParam);
            }
            if (countParam != null && !countParam.equals("")) {
                count = Integer.valueOf(countParam);
                count = count > 50 ? 50 : count;
            }
        } catch (NumberFormatException e) {
            logger.debug("bad query when search: " + e.getMessage());
            throw new BadRequestException("bad query format when search: " + e.getMessage());
        }

        List<CommentTrack> results = new ArrayList<CommentTrack>();

        try {
            results = commentService.listByReviewId(review_id, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list comment by reviewId error: " + e.getMessage());
        }

        return JSON.toJSONString(results);
    }

    @RequestMapping(value = "/{comment_id}/like", method = RequestMethod.POST)
    public String like(@PathVariable Long comment_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            commentService.like(token, comment_id);
        } catch (ConflictException e) {
            logger.debug("cannot like twice: " + e.getMessage());
            throw new exception.http.ConflictException("cannot like twice: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("like comment error: " + e.getMessage());
        }

        return "";
    }


    @RequestMapping(value = "/{comment_id}/like", method = RequestMethod.DELETE)
    public String cancelLike(@PathVariable Long comment_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            commentService.cancelLike(token, comment_id);
        } catch (NotFoundException e) {
            logger.debug("previous 'like' not found:  " + e.getMessage());
            throw new exception.http.NotFoundException("previous 'like' not found:  " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("like comment error: " + e.getMessage());
        }

        return "";
    }

}
