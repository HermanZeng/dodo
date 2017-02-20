package api.admin;

import com.alibaba.fastjson.JSON;
import entity.User;
import exception.http.BadRequestException;
import exception.http.UnknownException;
import exception.service.ConflictException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import service.UserService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 6/27/2016.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/admin")
public class AdminUsersResource {

    @Autowired
    HttpServletRequest request;
    private static final UserService userService = SpringIocUtil.getBean("userService", UserService.class);
    private static final Logger logger = LogManager.getLogger();

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String list() {

        String startParam = request.getParameter("start");
        String countParam = request.getParameter("count");

        int start = 0;
        int count = 20;
        if (startParam != null && !startParam.equals("")) {
            start = Integer.valueOf(startParam);
        }
        if (countParam != null && !countParam.equals("")) {
            count = Integer.valueOf(countParam);
            count = count > 100 ? 100 : count;
        }

        List<User> users = null;
        try {
            users = userService.findAllUser(start, count);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("list users error: " + e.getMessage());
        }
        return JSON.toJSONString(users);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String create(@RequestBody User req_user) {

        User newUser = null;
        try {
            newUser = userService.addUser(req_user);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("create user error: " + e.getMessage());
        } catch (ConflictException e) {
            logger.debug("user conflict when create new user");
            throw new exception.http.ConflictException("create user error: " + e.getMessage());
        } catch (IllegalInputException e) {
            logger.debug("illegal request body");
            throw new BadRequestException(e.getMessage());
        }

        newUser.setBooks(null);
        newUser.setPassword(null);

        return JSON.toJSONString(newUser);
    }

    @RequestMapping(value = "/user/{req_id}", method = RequestMethod.PUT)
    public String update(@PathVariable String req_id, @RequestBody User req_user) {

        User updatedUser = null;
        try {
            updatedUser = userService.updateUser(req_id, req_user);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("update user error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("user not found when update user.");
            throw new exception.http.NotFoundException("update user error: " + e.getMessage());
        } catch (IllegalInputException e) {
            logger.debug("illegal request body");
            throw new BadRequestException(e.getMessage());
        }

        return JSON.toJSONString(updatedUser);
    }

    @RequestMapping(value = "/user/{req_id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String req_id) {

        try {
            userService.deleteUser(req_id);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("delete user error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("user not found when delete user");
            throw new exception.http.NotFoundException("delete user error: " + e.getMessage());
        }

        return "";
    }

    @RequestMapping(value = "/user/{req_id}", method = RequestMethod.GET)
    public String show(@PathVariable String req_id) {

        User user = null;
        try {
            user = userService.findUserById(req_id);
        } catch (ExecuteException e) {
            e.printStackTrace();
            throw new UnknownException("show user detail error: " + e.getMessage());
        } catch (NotFoundException e) {
            logger.debug("user not found when show user detail");
            throw new exception.http.NotFoundException("show user error: " + e.getMessage());
        }

        return JSON.toJSONString(user);
    }

}
