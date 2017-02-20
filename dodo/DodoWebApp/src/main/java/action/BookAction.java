package action;

import com.sun.jmx.mbeanserver.SunJmxMBeanServer;
import entity.Author;
import entity.Book;
import entity.Category;
import entity.Translator;
import org.apache.struts2.json.annotations.JSON;
import service.base.Connection;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import service.base.Service;
import util.JsonUtil;
import util.SpringIocUtil;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fan on 7/13/2016.
 */
public class BookAction extends ActionSupport {
    private static final Logger logger = LogManager.getLogger();
    private Map<String, Object> jsonResult = new HashMap<String, Object>();

    private String bookstr;
    private String ID;

    public String addBook() {
        Book book = JsonUtil.jsonToObject(bookstr, Book.class);
        System.out.println(ServletActionContext.getRequest().getParameterMap().toString());
        System.out.println(book.toString());

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        if (!Book.validWid(book.getWid())) {
            jsonResult.put("msg", "invalid work id.");
            return SUCCESS;
        }

        if (!Book.validIsbn10(book.getIsbn10())) {
            jsonResult.put("msg", "invalid ISBN 10.");
            return SUCCESS;
        }

        if (!Book.validIsbn13(book.getIsbn13())) {
            jsonResult.put("msg", "invalid ISBN 13.");
            return SUCCESS;
        }

        if (!Book.validTitle(book.getTitle())) {
            jsonResult.put("msg", "invalid title.");
            return SUCCESS;
        }

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            book = service.books().create(book).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.CONFLICT) {
                logger.debug("addBook: conflict.");
                jsonResult.put("msg", "book conflict.");
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                logger.error("addBook: bad request.");
                jsonResult.put("msg", "bad input.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("addBook: server error.");
                jsonResult.put("msg", "internal error.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
            return SUCCESS;
        }
        jsonResult.put("success", true);
        return SUCCESS;
    }

    public String deleteBook() {

        System.out.println(ID);
        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            service.books().delete(ID).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.NOT_FOUND) {
                logger.debug("delete Book: book not found");
                jsonResult.put("msg", "delete Book: book not found");
                return SUCCESS;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("delete Book: server error");
                jsonResult.put("msg", "delete Book: server error");
                return SUCCESS;
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
        }
        System.out.println("done");
        jsonResult.put("success", true);
        return SUCCESS;
    }

    public String showBook(){

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        Book book = null;
        try {
            book = service.books().show(ID).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.NOT_FOUND) {
                logger.debug("show Book: book not found");
                jsonResult.put("msg", "book not found.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("show Book: server error");
                jsonResult.put("msg", "internal error.");
            }
        }
        jsonResult.put("book", JsonUtil.objectToJson(book));
        return SUCCESS;
    }

    public String updateBook(){

        Book book = JsonUtil.jsonToObject(bookstr, Book.class);

        String bookId = book.getId();
        System.out.println(bookId);
        System.out.println(book);

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        if (!Book.validWid(book.getWid())) {
            jsonResult.put("msg", "invalid work id.");
            return SUCCESS;
        }

        if (!Book.validIsbn10(book.getIsbn10())) {
            jsonResult.put("msg", "invalid ISBN 10.");
            return SUCCESS;
        }

        if (!Book.validIsbn13(book.getIsbn13())) {
            jsonResult.put("msg", "invalid ISBN 13.");
            return SUCCESS;
        }

        if (!Book.validTitle(book.getTitle())) {
            jsonResult.put("msg", "invalid title.");
            return SUCCESS;
        }

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);

        try {
            book = service.books().update(bookId, book).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.CONFLICT) {
                logger.debug("update Book: conflict");
                jsonResult.put("msg", "book conflict.");
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                logger.error("update Book: bad request");
                jsonResult.put("msg", "bad input.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("update Book: server error");
                jsonResult.put("msg", "internal error.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
            return SUCCESS;
        }
        jsonResult.put("success", true);
        return SUCCESS;
    }

    private Book[] books;
//    private Book[] books = new Book[2];
    private int start;
    private int count;
    public String listBook() {
//
//        Book book2 = new Book();
//        book2.setId("2334-9fj3-o138");
//        book2.setWid("2334-9fj3-o138");
//        book2.setTitle("睡美人");
//        book2.setIntroduction("天上的星星眨呀眨...");
//        book2.setPublisher("长江文艺出版社");
//        book2.setPages(214);
//        book2.setIsbn10("3746589073");
//        book2.setIsbn13("0938465372819");
//        book2.setImage("www.baidu.com?id=029ufj");
//        book2.setRate(9.1);
//
//        Category category1 = new Category();
//        category1.setDescription("工具书");
//        category1.setId(3);
//        category1.setReference(3);
//        Category category2 = new Category();
//        category2.setDescription("文学");
//        category2.setId(0);
//        category2.setReference(0);
//
//        Author author1 = new Author();
//        author1.setId("asu9823hfuh2");
//        author1.setName("刘慈欣");
//        author1.setNationality("中");
//        Author author2 = new Author();
//        author2.setId("4981fj13");
//        author2.setName("列夫·托尔斯泰");
//        author2.setNationality("俄");
//
//        Translator translator1 = new Translator();
//        translator1.setId("3948fjq2");
//        translator1.setName("傅雷");
//
//        List<Category> categories2 = new ArrayList<Category>();
//        categories2.add(category1);
//        book2.setCategory(categories2);
//        List<Author> authors2 = new ArrayList<Author>();
//        authors2.add(author1);
//        authors2.add(author2);
//        book2.setAuthors(authors2);
//        List<Translator> translators2 = new ArrayList<Translator>();
//        translators2.add(translator1);
//        book2.setTranslators(translators2);
//
//        Book book1 = new Book();
//        book1.setId("asdf-823f-jafi");
//        book1.setWid("asdf-823f-jafi");
//        book1.setTitle("红楼梦");
//        book1.setIntroduction("门泊东吴万里船...");
//        book1.setPublisher("青年出版社");
//        book1.setPages(3210);
//        book1.setIsbn10("7365120923");
//        book1.setIsbn13("9564287109387");
//        book1.setImage("www.baidu.com?id=sadf6");
//        book1.setRate(8.7);
//        List<Category> categories1 = new ArrayList<Category>();
//        categories1.add(category2);
//        List<Author> authors1 = new ArrayList<Author>();
//        authors1.add(author1);
//        book1.setCategory(categories1);
//        book1.setAuthors(authors1);
//
//        books[0] = book1;
//        books[1] = book2;

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");
        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);

        try {
            books = service.books().list(start, count).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("list Book: server error");
                jsonResult.put("msg", "list Book: server error");
                return SUCCESS;
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
        }
        jsonResult.put("books", JsonUtil.objectToJson(books));
        System.out.println(JsonUtil.objectToJson(books));
        return SUCCESS;
    }

    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(Map<String, Object> jsonResult) {
        this.jsonResult = jsonResult;
    }


    public Book[] getBooks() {
        return books;
    }

    public void setBooks(Book[] books) {
        this.books = books;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBookstr() {
        return bookstr;
    }

    public void setBookstr(String bookstr) {
        this.bookstr = bookstr;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
