package api;

import com.alibaba.fastjson.JSON;
import entity.BookReviewLong;
import entity.Track;
import exception.http.BadRequestException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.BookReviewLongService;
import service.TrackService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fan on 7/19/2016.
 */
@RestController
@RequestMapping(value = "/long_book_review")
public class BookReviewLongResource {
    private static final Logger logger = LogManager.getLogger();
    private static final BookReviewLongService reviewService = SpringIocUtil.getBean("bookReviewLongService", BookReviewLongService.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@RequestBody BookReviewLong reqReview) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        BookReviewLong review = null;
        try {
            review = reviewService.add(token, reqReview);
        } catch (IllegalInputException e) {
            logger.debug("illegal request body: " + e.getMessage());
            throw new BadRequestException("illegal request body: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("create long book review error: " + e.getMessage());
        }

        return JSON.toJSONString(review);
    }

    @RequestMapping(value = "/{review_id}", method = RequestMethod.GET)
    public String show(@PathVariable Long review_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        BookReviewLong review = null;
        try {
            review = reviewService.get(review_id);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("show long book review error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("long book review not found when show: " + e.getMessage());
            throw new exception.http.NotFoundException("long book review not found when show:  " + e.getMessage());
        }

        return JSON.toJSONString(review);

    }

    @RequestMapping(value = "/{review_id}", method = RequestMethod.PUT)
    public String edit(@PathVariable Long review_id, @RequestBody BookReviewLong reqReview) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }
        System.out.println(reqReview);
        BookReviewLong review;

        try {
            review = reviewService.update(token, review_id, reqReview);
        } catch (IllegalInputException e) {
            logger.debug("illegal request body: " + e.getMessage());
            throw new BadRequestException("illegal request body: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("long book review not found when update: " + e.getMessage());
            throw new exception.http.NotFoundException("long book review not found when update:  " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("cannot edit others review: " + e.getMessage());
            throw new exception.http.ForbiddenException("cannot edit others review: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("edit long book review error: " + e.getMessage());
        }

        return JSON.toJSONString(review);
    }

    @RequestMapping(value = "/{review_id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long review_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            reviewService.delete(token, review_id);
        } catch (NotFoundException e) {
            logger.debug("long book review not found when delete: " + e.getMessage());
            throw new exception.http.NotFoundException("long book review not found when delete:  " + e.getMessage());
        } catch (ForbiddenException e) {
            logger.debug("cannot delete others review: " + e.getMessage());
            throw new exception.http.ForbiddenException("cannot delete others review: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("delete long book review error: " + e.getMessage());
        }

        return "";
    }


    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public String listByUser(@PathVariable String user_id) {

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

        List<BookReviewLong> results = new ArrayList<BookReviewLong>();

        try {
            results = reviewService.listByReviewer(user_id, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list long book review by reviewer error: " + e.getMessage());
        }

        return JSON.toJSONString(results);
    }

    @RequestMapping(value = "/book/{wid}", method = RequestMethod.GET)
    public String listByWid(@PathVariable String wid) {

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

        List<BookReviewLong> results = new ArrayList<BookReviewLong>();

        try {
            results = reviewService.listByWid(wid, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list long book review by wid error: " + e.getMessage());
        }

        return JSON.toJSONString(results);
    }

    @RequestMapping(value = "/{review_id}/like", method = RequestMethod.POST)
    public String like(@PathVariable Long review_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            reviewService.like(token, review_id);
        } catch (ConflictException e) {
            logger.debug("cannot like twice: " + e.getMessage());
            throw new exception.http.ConflictException("cannot like twice: " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("like long book review error: " + e.getMessage());
        }

        return "";
    }


    @RequestMapping(value = "/{review_id}/like", method = RequestMethod.DELETE)
    public String cancelLike(@PathVariable Long review_id) {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            reviewService.cancelLike(token, review_id);
        } catch (NotFoundException e) {
            logger.debug("previous \'like\' not found:  " + e.getMessage());
            throw new exception.http.NotFoundException("previous \'like\' not found:  " + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("like long book review error: " + e.getMessage());
        }

        return "";
    }

}
