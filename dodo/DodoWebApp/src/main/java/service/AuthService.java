package service;

import entity.Access;
import entity.EmailPassword;
import entity.User;
import service.base.Connection;
import service.base.DodoApiRequest;
import org.springframework.http.HttpMethod;

/**
 * Created by fan on 7/1/2016.
 */
public class AuthService {
    private String url;
    private Connection connection;

    public AuthService(String url, Connection connection) {
        this.url = url;
        this.connection = connection;
    }

    public Login login(String email, String password) {
        EmailPassword body = new EmailPassword();
        body.setEmail(email);
        body.setPassword(password);
        return new Login(body);
    }

    public Logout logout(){
        return new Logout();
    }

    public Signup signup(User user){
        return new Signup(user);
    }

    public Validate validate(String code){
        return new Validate(code);
    }

    public class Login extends DodoApiRequest<EmailPassword, Access> {
        public Login(EmailPassword emailPassword) {
            super(connection, url, HttpMethod.POST, "/tokens", emailPassword, Access.class);
        }
    }

    public class Logout extends DodoApiRequest<Void, Void> {
        public Logout() {
            super(connection, url, HttpMethod.DELETE, "/tokens", null, Void.class);
        }
    }

    public class Signup extends DodoApiRequest<User, Void> {
        public Signup(User user) {
            super(connection, url, HttpMethod.POST, "/register", user, Void.class);
        }
    }
    public class Validate extends DodoApiRequest<Void, Void> {
        public Validate(String code) {
            super(connection, url, HttpMethod.GET, "/register?code="+code, null, Void.class);
        }
    }
}
