package service;

import dao.UserDAO;
import entity.User;
import exception.dao.ConflictException;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import exception.service.IllegalInputException;

import java.util.List;

/**
 * Created by fan on 6/28/2016.
 */
public class UserService {

    private static UserDAO userDAO;

    public User addUser(User user) {
        if (!validUser(user)) {
            throw new IllegalInputException("Invalid user input");
        }

        User newUser = null;
        try {
            newUser = userDAO.save(user);
            newUser.setBooks(null);
            newUser.setPassword(null);
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("Add user error: " + e.getMessage());
        } catch (ConflictException e) {
            throw new exception.service.ConflictException("Add user error: " + e.getMessage());
        }

        return newUser;
    }

    public void deleteUser(String userId) {
        try {
            userDAO.delete(userId);
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("Delete user error: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new exception.service.NotFoundException("Delete user error: " + e.getMessage());
        }
    }

    public User updateUser(String userId, User user) {
        if (!validUpdateUser(user)) {
            throw new IllegalInputException("Invalid user input");
        }

        User updatedUser = null;

        try {
            updatedUser = userDAO.update(userId, user);
            updatedUser.setBooks(null);
            updatedUser.setPassword(null);
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("Update user error: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new exception.service.NotFoundException("Update user error: " + e.getMessage());
        }

        return updatedUser;
    }

    public List<User> findAllUser(int start, int count) {
        List<User> users = null;
        try {
            users = userDAO.findAll(start, count);
            for (User u :
                    users) {
                u.setBooks(null);
                u.setPassword(null);
            }
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("findAll user error: " + e.getMessage());
        }
//        if (users == null) {
//            users = new ArrayList<User>();
//        } // unnecessary. findAll would not return null
        return users;
    }

    public User findUserById(String userId) {
        User user = null;
        try {
            user = userDAO.find(userId);
            user.setBooks(null);
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("findUserById error: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new exception.service.NotFoundException("findUserById error: " + e.getMessage());
        }
        return user;
    }

    public User findUserByEmail(String email) {
        if (!validEmail(email)) {
            throw new IllegalInputException("Invalid email");
        }

        User user = null;
        try {
            user = userDAO.findByEmail(email);
            user.setBooks(null);
        } catch (ExecuteException e) {
            throw new exception.service.ExecuteException("findUserByEmail error: " + e.getMessage());
        } catch (NotFoundException e) {
            throw new exception.service.NotFoundException("findUserByEmail error: " + e.getMessage());
        }
        return user;

    }

    public boolean validEmail(String email) {
        if (email == null) return false;

        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        return email.matches(regex);
    }

    public boolean validName(String name) {
        if (name == null) return false;

        String regex = "^([a-zA-Z0-9]|[\\u4e00-\\u9fa5]|_){3,30}$";

        return name.matches(regex);
    }

    public boolean validPassword(String password){
        if (password == null) return false;

        String regex = "^\\w{6,18}";

        return password.matches(regex);
    }

    public boolean validUser(User user) {
        return user != null && validEmail(user.getEmail()) && validName(user.getFirstname()) && validName(user.getLastname()) && validPassword(user.getPassword());

    }

    public boolean validUpdateUser(User user) {
        return user != null && validEmail(user.getEmail()) && validName(user.getFirstname()) && validName(user.getLastname()) ;

    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
