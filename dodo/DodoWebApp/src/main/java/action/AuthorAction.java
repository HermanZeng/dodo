package action;

import com.opensymphony.xwork2.ActionSupport;
import entity.Author;
import entity.Book;
import entity.Category;
import entity.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import service.base.Connection;
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
public class AuthorAction extends ActionSupport {
    private static final Logger logger = LogManager.getLogger();

    private String q;
    private Author[] authors;
//    private Author[] authors = new Author[2];
    public String search() {
//        Author author1 = new Author();
//        author1.setId("2042f2iej");
//        author1.setNationality("中");
//        author1.setName("路遥");
//        Author author2 = new Author();
//        author2.setId("4981fj13");
//        author2.setName("列夫·托尔斯泰");
//        author2.setNationality("俄");
//
//        authors[0] = author1;
//        authors[1] = author2;

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");
        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            authors = service.authors().list(q, 0, 5).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("list author: server error");
                return SUCCESS;
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
        }

        System.out.println(JsonUtil.objectToJson(authors));
        return SUCCESS;
    }


    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
