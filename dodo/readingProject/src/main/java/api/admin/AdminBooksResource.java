package api.admin;

import com.alibaba.fastjson.JSON;
import entity.Book;
import exception.http.BadRequestException;
import exception.http.UnknownException;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import service.AdminBookService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 6/27/2016.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/admin/book")
public class AdminBooksResource {

    @Autowired
    HttpServletRequest request;
    private static final AdminBookService bookService = SpringIocUtil.getBean("adminBookService", AdminBookService.class);
    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list() {

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");

        int start = 0;
        int count = 20;
        if (startParam != null && !startParam.equals("")) {
            start = Integer.valueOf(startParam);
        }
        if (countParam != null && !countParam.equals("")) {
            count = Integer.valueOf(countParam);
            count = count > 100 ? 100 : count;
        }

        List<Book> books = null;
        try {
            books = bookService.findAllBook(start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list book error: " + e.getMessage());
        }
        return JSON.toJSONString(books);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String create(@RequestBody Book req_book) {

        Book newBook = null;
        try {
            newBook = bookService.addBook(req_book);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("create book error: " + e.getMessage());
        } catch (ConflictException e) {
            logger.debug("book conflict when create new book");
            throw new exception.http.ConflictException("create book error: " + e.getMessage());
        } catch (IllegalInputException e) {
            logger.debug("illegal request body");
            throw new BadRequestException(e.getMessage());
        }

        return JSON.toJSONString(newBook);
    }

    @RequestMapping(value = "/{req_id}", method = RequestMethod.PUT)
    public String update(@PathVariable String req_id, @RequestBody Book req_book) {

        Book updateBook = null;
        try {
            updateBook = bookService.updateBook(req_id, req_book);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("update book error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("book not found when update book.");
            throw new exception.http.NotFoundException("update book error: " + e.getMessage());
        } catch (IllegalInputException e) {
            logger.debug("illegal request body");
            throw new BadRequestException(e.getMessage());
        }

        return JSON.toJSONString(updateBook);
    }

    @RequestMapping(value = "/{req_id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String req_id) {

        try {
            bookService.deleteBook(req_id);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("delete book error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("book not found when delete book");
            throw new exception.http.NotFoundException("delete book error: " + e.getMessage());
        }

        return "";
    }

//    @RequestMapping(value = "/{req_id}", method = RequestMethod.GET)
//    public String show(@PathVariable String req_id) {
//
//        User user = null;
//        try {
//            user = userService.findUserById(req_id);
//        } catch (ExecuteException e) {
//            e.printStackTrace();
//            throw new UnknownException("show user detail error: " + e.getMessage());
//        } catch (NotFoundException e) {
//            logger.debug("user not found when show user detail");
//            throw new exception.http.NotFoundException("show user error: " + e.getMessage());
//        }
//
//        return JSON.toJSONString(user);
//    }

}
