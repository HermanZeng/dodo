package entity;

/**
 * Created by fan on 7/1/2016.
 */
public class Access {

    private String token;

    private User user;

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
        return "Access{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }

}
