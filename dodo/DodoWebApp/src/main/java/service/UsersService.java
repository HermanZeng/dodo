package service;

import entity.User;
import service.base.Connection;
import service.base.DodoApiRequest;
import org.springframework.http.HttpMethod;

/**
 * Created by fan on 7/1/2016.
 */
public class UsersService {
    private Connection connection;
    private String url;

    public UsersService(String url, Connection connection) {
        this.url = url;
        this.connection = connection;
    }

    public Create create(User user) {
        return new Create(user);
    }

    public Show show(String userId) {
        return new Show(userId);
    }

    public Update update(String userId, User user) {
        return new Update(userId, user);
    }

    public Delete delete(String userId) {
        return new Delete(userId);
    }

    public List list(int start, int count) {
        return new List(start, count);
    }

    public class Create extends DodoApiRequest<User, User> {
        public Create(User user) {
            super(connection, url, HttpMethod.POST, "", user, User.class);
        }
    }

    public class Show extends DodoApiRequest<Void, User> {
        public Show(String userId) {
            super(connection, url, HttpMethod.GET, "/" + userId, null, User.class);
        }
    }

    public class Update extends DodoApiRequest<User, User> {
        public Update(String userId, User user) {
            super(connection, url, HttpMethod.PUT, "/" + userId, user, User.class);
        }
    }

    public class Delete extends DodoApiRequest<Void, Void> {
        public Delete(String userId) {
            super(connection, url, HttpMethod.DELETE, "/" + userId, null, Void.class);
        }
    }

    public class List extends DodoApiRequest<Void, User[]> {
        public List(int start, int count) {
            super(connection, url, HttpMethod.GET, "?start=" + start + "&count=" + count, null, User[].class);
        }
    }

}
