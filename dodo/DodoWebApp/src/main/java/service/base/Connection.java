package service.base;

import entity.User;

/**
 * Created by fan on 7/1/2016.
 */
public class Connection {

    private User user;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
