package action;

import com.opensymphony.xwork2.ActionSupport;
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

/**
 * Created by fan on 7/13/2016.
 */
public class TranslatorAction extends ActionSupport {
    private static final Logger logger = LogManager.getLogger();

    private String q;
    private Translator[] translators;
//    private Translator[] translators = new Translator[2];
    public String search() {
//        Translator translator1= new Translator();
//        translator1.setId("2042f2iej");
//        translator1.setNationality("中");
//        translator1.setName("路遥");
//        Translator translator2 = new Translator();
//        translator2.setId("4981fj13");
//        translator2.setName("列夫·托尔斯泰");
//        translator2.setNationality("俄");
//
//        translators[0] = translator1;
//        translators[1] = translator2;

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");
        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            translators = service.translators().list(q, 0, 5).request();
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

        System.out.println(JsonUtil.objectToJson(translators));
        return SUCCESS;
    }


    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public Translator[] getTranslators() {
        return translators;
    }

    public void setTranslators(Translator[] translators) {
        this.translators = translators;
    }
}
