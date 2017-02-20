package api;

import com.alibaba.fastjson.JSON;
import entity.Book;
import exception.http.UnknownException;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.BookshelfService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by heming on 7/13/2016.
 */

@RestController
@RequestMapping(value = "/bookshelf")
public class BookshelfResource {
    private final BookshelfService bookshelfService = SpringIocUtil.getBean("bookshelfService", BookshelfService.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/books/{book_id}", method = RequestMethod.POST)
    @ResponseBody
    public String addBook(@PathVariable String book_id) {
        String token = request.getHeader("X-Auth-Token");
        Book book = null;

        try {
            book = bookshelfService.addBook(token, book_id);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("Internal error:" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.http.NotFoundException("Book not found" + ne.getMessage());
        } catch (ConflictException ce) {
            ce.printStackTrace();
            throw new exception.http.ConflictException("Book already exists in bookshelf" + ce.getMessage());
        }

        return JSON.toJSONString(book);
    }

    @RequestMapping(value = "/books/{book_id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBook(@PathVariable String book_id) {
        String token = request.getHeader("X-Auth-Token");

        try {
            bookshelfService.deleteBook(token, book_id);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("Internal error" + ee.getMessage());
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.http.NotFoundException("Book does not exist in bookshelf" + ne.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    @ResponseBody
    public String list() {
        String token = request.getHeader("X-Auth-Token");
        Set<Book> bookSet = null;

        try {
            bookSet = bookshelfService.listAll(token);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("Intrenal error" + ee.getMessage());
        }

        if (bookSet != null) {
            return JSON.toJSONString(bookSet);
        } else {
            return "";
        }
    }
}
