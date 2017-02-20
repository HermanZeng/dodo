package api;

import com.alibaba.fastjson.JSON;
import entity.Book;
import exception.http.BadRequestException;
import exception.http.UnknownException;
import exception.service.ExecuteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.UserBookService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 7/22/2016.
 */

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/book")
public class BookResource {
    @Autowired
    HttpServletRequest request;

    private final UserBookService userBookService = SpringIocUtil.getBean("userBookService", UserBookService.class);

    @RequestMapping(value = "/search/title", method = RequestMethod.GET)
    public String searchBookByTitle() {
        String title = request.getParameter("title");

        if ((title == null) || (title == "")) {
            throw new BadRequestException("title not found in request");
        }

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

        List<Book> bookList = null;

        try {
            bookList = userBookService.searchBookByTitle(title, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("find books by title error: " + ee.getMessage());
        }

        return JSON.toJSONString(bookList);
    }

    @RequestMapping(value = "/search/isbn", method = RequestMethod.GET)
    public String searchBookByIsbn() {
        String isbn = request.getParameter("isbn");

        if ((isbn == null) || (isbn == "")) {
            throw new BadRequestException("isbn not found in request");
        }

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

        List<Book> bookList = null;

        try {
            bookList = userBookService.searchBookByIsbn(isbn, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("find books by ISBN error: " + ee.getMessage());
        }

        return JSON.toJSONString(bookList);
    }

    @RequestMapping(value = "/search/category", method = RequestMethod.GET)
    public String searchBookByCategory() {
        String category = request.getParameter("category");

        if ((category == null) || (category == "")) {
            throw new BadRequestException("isbn not found in request");
        }

        int categoryId = Integer.valueOf(category);

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

        List<Book> bookList = null;

        try {
            bookList = userBookService.searchBookByCategory(categoryId, start, count);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("find books by category error: " + ee.getMessage());
        }

        return JSON.toJSONString(bookList);
    }
}
