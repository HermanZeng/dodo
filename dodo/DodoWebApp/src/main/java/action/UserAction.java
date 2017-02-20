package action;

import entity.Role;
import entity.User;
import service.base.Connection;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import service.base.Service;
import util.SpringIocUtil;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by fan on 7/1/2016.
 */
public class UserAction extends ActionSupport {
    private static final Logger logger = LogManager.getLogger();
    private Map<String, Object> jsonResult = new HashMap<String, Object>();

    private User user;

    public String addUser() {
        System.out.println(ServletActionContext.getRequest().getParameterMap().toString());
        System.out.println(user.toString());

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        if (!User.validEmail(user.getEmail())) {
            jsonResult.put("msg", "invalid email.");
            return SUCCESS;
        }

        if (!User.validName(user.getFirstName())) {
            jsonResult.put("msg", "invalid first name.");
            return SUCCESS;
        }

        if (!User.validName(user.getLastName())) {
            jsonResult.put("msg", "invalid last name.");
            return SUCCESS;
        }

        if (!User.validPassword(user.getPassword())) {
            jsonResult.put("msg", "invalid password.");
            return SUCCESS;
        }

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            user = service.user().create(user).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.CONFLICT) {
                logger.debug("addUser: conflict.");
                jsonResult.put("msg", "user conflict.");
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                logger.error("addUser: bad request.");
                jsonResult.put("msg", "bad input.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("addUser: server error.");
                jsonResult.put("msg", "internal error.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
            return SUCCESS;
        }
        jsonResult.put("success", true);
        return SUCCESS;
    }


    public String deleteUser() {
        String userId = user.getUserId();
        System.out.println(userId);

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            service.user().delete(userId).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.NOT_FOUND) {
                logger.debug("delete User: user not found");
                jsonResult.put("msg", "delete User: user not found");
                return SUCCESS;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("delete User: server error");
                jsonResult.put("msg", "delete User: server error");
                return SUCCESS;
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
        }
        System.out.println("done");
        jsonResult.put("success", true);
        return SUCCESS;
    }

//    public String showUser() {
//        String userId = user.getUserId();
//
//        HttpSession session = ServletActionContext.getRequest().getSession();
//        Connection conn = (Connection) session.getAttribute("connection");
//
//        Service service = SpringIocUtil.getBean("linkedService");
//        service.setConn(conn);
//        try {
//            user = service.user().show(userId).request();
//        } catch (HttpStatusCodeException e) {
//            HttpStatus statusCode = e.getStatusCode();
//            logger.debug(statusCode.name());
//
//            if (statusCode == HttpStatus.NOT_FOUND) {
//                logger.debug("show User: user not found");
//                return INPUT;
//            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
//                return LOGIN;
//            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
//                logger.error("show User: server error");
//                return ERROR;
//            }
//        }
//
//        return SUCCESS;
//    }

    public String updateUser() {
        String userId = user.getUserId();
        System.out.println(userId);

        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");

        System.out.println(ServletActionContext.getRequest().getParameterMap().toString());
        System.out.println(user.toString());

        if (!User.validEmail(user.getEmail())) {
            jsonResult.put("msg", "invalid email.");
            return SUCCESS;
        }

        if (!User.validName(user.getFirstName())) {
            jsonResult.put("msg", "invalid first name.");
            return SUCCESS;
        }

        if (!User.validName(user.getLastName())) {
            jsonResult.put("msg", "invalid last name.");
            return SUCCESS;
        }

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);

        try {
            user = service.user().update(userId, user).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.CONFLICT) {
                logger.debug("update User: conflict");
                jsonResult.put("msg", "user conflict.");
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                logger.error("update User: bad request");
                jsonResult.put("msg", "bad input.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("update User: server error");
                jsonResult.put("msg", "internal error.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
            return SUCCESS;
        }
        jsonResult.put("success", true);
        return SUCCESS;
    }

    private User[] users;
    private int start;
    private int count;

    public String listUser() {

//        Role admin = new Role();
//        admin.setDescription("admin");
//        admin.setReference(1);
//        admin.setRoleId(1);
//        Role vip = new Role();
//        vip.setDescription("VIP");
//        vip.setReference(2);
//        vip.setRoleId(2);
//
//        User user1 = new User();
//        user1.setUserId("fa32fsafgq2");
//        user1.setFirstName("fan");
//        user1.setLastName("yang");
//        user1.setEmail("dsaf@163.com");
//        Set<Role> roles1 = new HashSet<Role>();
//        roles1.add(admin);
//        roles1.add(vip);
//        user1.setRoles(roles1);
//
//        User user2 = new User();
//        user2.setUserId("89oi23iji3o");
//        user2.setFirstName("kevin");
//        user2.setLastName("ling");
//        user2.setEmail("dodo@amazon.com");
//        users[0] = user1;
//        users[1] = user2;
        HttpSession session = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) session.getAttribute("connection");
        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);

        try {
            users = service.user().list(start, count).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            logger.debug(statusCode.name());

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("list User: server error");
                jsonResult.put("msg", "list User: server error");
                return ERROR;
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                return LOGIN;
            }
        }
        jsonResult.put("success", true);
        return SUCCESS;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(Map<String, Object> jsonResult) {
        this.jsonResult = jsonResult;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }
}
