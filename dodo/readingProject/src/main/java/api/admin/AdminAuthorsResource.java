package api.admin;

import com.alibaba.fastjson.JSON;
import entity.Author;
import exception.http.UnknownException;
import exception.service.ExecuteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.AdminAuthorService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 6/27/2016.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/admin/author")
public class AdminAuthorsResource {

    @Autowired
    HttpServletRequest request;
    private static final AdminAuthorService service = new AdminAuthorService();
    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String list() {

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");
        String queryParam = request.getParameter("q");

        int start = 0;
        int count = 20;
        if (startParam != null && !startParam.equals("")) {
            start = Integer.valueOf(startParam);
        }
        if (countParam != null && !countParam.equals("")) {
            count = Integer.valueOf(countParam);
            count = count > 100 ? 100 : count;
        }

        List<Author> authors = null;
        try {
            authors = service.findByName(queryParam, start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list authors error: " + e.getMessage());
        }
        System.out.println(authors);
        return JSON.toJSONString(authors);
    }

}
