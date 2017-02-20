package entity;

/**
 * Created by fan on 6/28/2016.
 */
public class Access {
    private String token;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.user.setPassword(null);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Access{" +
                "token='" + token + '\'' +
                '}';
    }
}
