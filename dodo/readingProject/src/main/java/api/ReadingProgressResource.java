package api;

import com.alibaba.fastjson.JSON;
import entity.ReadingLog;
import entity.ReadingProgress;
import exception.http.BadRequestException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.ReadingProgressService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fan on 7/18/2016.
 */
@RestController
@RequestMapping(value = "/progress")
public class ReadingProgressResource {
    private static final Logger logger = LogManager.getLogger();
    private static final ReadingProgressService progressService = SpringIocUtil.getBean("readingProgressService", ReadingProgressService.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/{book_id}/total/{total_number}", method = RequestMethod.PUT)
    public String setTotal(@PathVariable String book_id, @PathVariable Integer total_number) {
        logger.debug("book_id: " + book_id + ", total_number" + total_number);

        if (!validBookId(book_id)) {
            logger.debug("Bad request. incorrect book_id");
            throw new BadRequestException("Bad request. incorrect book_id");
        }

        if (!validTotalNumber(total_number)) {
            logger.debug("Bad request. incorrect total number");
            throw new BadRequestException("Bad request. incorrect total number");
        }
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            progressService.setTotalPages(token, book_id, total_number);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("set total pages error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("book not found when set total pages: " + e.getMessage());
            throw new exception.http.NotFoundException("book not found when set total pages: " + e.getMessage());
        } catch (IllegalInputException e) {
            logger.debug("illegal total number: " + e.getMessage());
            throw new exception.http.BadRequestException("illegal total number: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/{book_id}/{current_page}", method = RequestMethod.POST)
    public String addProgress(@PathVariable String book_id, @PathVariable Integer current_page) {
        logger.debug("book_id: " + book_id + ", current_page: " + current_page);

        if (!validBookId(book_id)) {
            logger.debug("Bad request. incorrect book_id");
            throw new BadRequestException("Bad request. incorrect book_id");
        }

        if (!validCurrentPage(current_page)) {
            logger.debug("Bad request. incorrect current page");
            throw new BadRequestException("Bad request. incorrect current page");
        }

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            progressService.addProgress(token, book_id, current_page);
        } catch (IllegalInputException e) {
            logger.debug("bad request. illegal input" + e.getMessage());
            throw new BadRequestException("bad request. illegal input" + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("book not found when add reading progress." + e.getMessage());
            throw new exception.http.NotFoundException("book not found when add reading progress." + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("add progress error: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/{book_id}", method = RequestMethod.GET)
    public String showHistory(@PathVariable String book_id) {
        logger.debug("book_id: " + book_id);

        if (!validBookId(book_id)) {
            logger.debug("Bad request. incorrect book_id");
            throw new BadRequestException("Bad request. incorrect book_id");
        }

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");

        int start = 0;
        int count = 10;
        if (startParam != null && !startParam.equals("")) {
            start = Integer.valueOf(startParam);
        }
        if (countParam != null && !countParam.equals("")) {
            count = Integer.valueOf(countParam);
            count = count > 100 ? 100 : count;
        }

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        List<ReadingLog> bookLogs = null;

        try {
            bookLogs = progressService.findReadingLog(token, book_id, start, count);
        } catch (NotFoundException e) {
            logger.debug("book not found when showing reading log." + e.getMessage());
            throw new exception.http.NotFoundException("book not found when showing reading log." + e.getMessage());
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("show reading log error: " + e.getMessage());
        }

        return JSON.toJSONString(bookLogs);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getCurrentProgress() {

        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        List<ReadingProgress> myProgress = null;

        try {
            myProgress = progressService.findAllProgress(token);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("get current progress error" + e.getMessage());
        }

        return JSON.toJSONString(myProgress);
    }

    private boolean validBookId(String bookId) {
        if (bookId.matches("(\\w+-\\w+)+")) {
            return true;
        }
        return false;
    }

    private boolean validTotalNumber(Integer totalNumber) {
        if (totalNumber == null) {
            return false;
        }
        if (totalNumber < 0 || totalNumber > 100000000) {
            return false;
        }
        return true;
    }

    private boolean validCurrentPage(Integer currentPage) {
        if (currentPage == null) {
            return false;
        }
        if (currentPage < 0) {
            return false;
        }
        return true;
    }


}
