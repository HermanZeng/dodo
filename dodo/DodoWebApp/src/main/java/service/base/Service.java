package service.base;

import service.*;

/**
 * Created by fan on 7/1/2016.
 */
public class Service {
    private String apiUrl;
    private Connection conn = null;

    public Service(String apiUrl) {
        this.apiUrl = apiUrl;
        this.conn = null;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public AuthService auth() {
        return new AuthService(apiUrl + "/auth", conn);
    }

    public UsersService user() {
        return new UsersService(apiUrl + "admin/user", conn);
    }

    public BooksService books() {
        return new BooksService(apiUrl + "admin/book", conn);
    }

    public AuthorsService authors() {
        return new AuthorsService(apiUrl + "admin/author", conn);
    }

    public TranslatorsService translators() {
        return new TranslatorsService(apiUrl + "admin/translator", conn);
    }
}
