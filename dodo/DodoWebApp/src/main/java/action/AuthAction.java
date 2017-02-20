package action;

import entity.Access;
import entity.Role;
import entity.User;
import service.base.Connection;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import service.base.Service;
import util.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by fan on 7/1/2016.
 */
public class AuthAction extends ActionSupport {

    private String loginEmail;
    private String loginPassword;


    public String login() {

        HttpSession userSession = ServletActionContext.getRequest().getSession();
        if (userSession.getAttribute("connection") != null) {
            return SUCCESS;
        }

        if (!User.validEmail(loginEmail)) {
            return INPUT;
        }

        if (!User.validPassword(loginPassword)) {
            return INPUT;
        }

        Service service = SpringIocUtil.getBean("linkedService");
        Access access = null;
        try {
            access = service.auth().login(loginEmail, loginPassword).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println(statusCode.name());
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                // wrong pwd
            } else if (statusCode == HttpStatus.NOT_FOUND) {
                // user not found
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
                // input illegal
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                // server error, log it
                return ERROR;
            }
            return INPUT;
        }

        Set<Role> roles = access.getUser().getRoles();
        boolean isAdmin = false;
        for (Role role : roles) {
            if (role.getReference() == 1) isAdmin = true;
        }
        if (!isAdmin) {
            System.out.println("not admin");
            return LOGIN;
        }

        Connection conn = new Connection();
        conn.setToken(access.getToken());
        conn.setUser(access.getUser());

        userSession.setAttribute("connection", conn);
        return SUCCESS;

    }

    public String logout() {
        HttpSession userSession = ServletActionContext.getRequest().getSession();
        Connection conn = (Connection) userSession.getAttribute("connection");
        System.out.println("user session:" + conn);

        Service service = SpringIocUtil.getBean("linkedService");
        service.setConn(conn);
        try {
            service.auth().logout().request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println(statusCode.name());
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                // not login yet
                return LOGIN;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                // server error, log it
                return ERROR;
            }
        }
        userSession.invalidate();

        return SUCCESS;
    }

    private User signupUser;


    public String signup() {
        if (!User.isValid(signupUser)) {
            return INPUT;
        }
        Service service = SpringIocUtil.getBean("linkedService");
        try {
            service.auth().signup(signupUser).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println(statusCode.name());
            if (statusCode == HttpStatus.CONFLICT) {
            } else if (statusCode == HttpStatus.BAD_REQUEST) {
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                return ERROR;
            }
            return INPUT;
        }
        return SUCCESS;
    }

    public String validating() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String validationCode = request.getParameter("code");

        Service service = SpringIocUtil.getBean("linkedService");
        try {
            service.auth().validate(validationCode).request();
        } catch (HttpStatusCodeException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println(statusCode.name());
            if (statusCode == HttpStatus.NOT_FOUND) {
                // incorrect validation code
                return LOGIN;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
                // server error, log it
                return ERROR;
            }
        }

        return SUCCESS;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public User getSignupUser() {
        return signupUser;
    }

    public void setSignupUser(User signupUser) {
        this.signupUser = signupUser;
    }
}
