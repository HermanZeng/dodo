package api;

import com.alibaba.fastjson.JSON;
import entity.Access;
import entity.User;
import entity.UsernamePassword;
import exception.http.BadRequestException;
import exception.http.ConflictException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.security.GenerationException;
import exception.service.ExecuteException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import security.token.TokenManager;
import security.validationCode.ValidationCodeManager;
import service.UserService;
import utilities.SpringIocUtil;
import utilities.mail.MailConfig;
import utilities.mail.MailUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by fan on 6/28/2016.
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    private static final Logger logger = LogManager.getLogger();

    private static final UserService userService = SpringIocUtil.getBean("userService", UserService.class);
    private ValidationCodeManager vmanager = SpringIocUtil.getBean("validationCodeManager", ValidationCodeManager.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/tokens", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody UsernamePassword usernamePassword) {

        logger.debug("login: request: " + usernamePassword.toString());

        if (!userService.validPassword(usernamePassword.getPassword())){
            logger.debug("login: invalid password");
            throw new BadRequestException("invalid password");
        }

        User user;
        try {
            user = userService.findUserByEmail(usernamePassword.getEmail());
        } catch (NotFoundException e) {
            logger.debug("login: User name does not exist");
            throw new exception.http.NotFoundException("User name does not exist");
        } catch (IllegalInputException e) {
            logger.debug("login: invalid email: " + e.getMessage());
            throw new BadRequestException("invalid email" + e.getMessage());
        } catch (ExecuteException e) {
            logger.debug("login: execute error: " + e.getMessage());
            throw new UnknownException("invalid email" + e.getMessage());
        }

        logger.debug(user.toString());

        if (!user.getPassword().equals(usernamePassword.getPassword())){
            logger.debug("login: Password is incorrect");
            throw new UnauthorizedException("Password is incorrect");
        }

        String token;
        TokenManager tokenmanager = SpringIocUtil.getBean("tokenManager", TokenManager.class);
        try {
            token = tokenmanager.generate(user);
        } catch (GenerationException e) {
            e.printStackTrace();
            logger.error("login: Token generation failed");
            throw new UnknownException("Token generation failed");
        }

        Access access = new Access();
        access.setToken(token);
        access.setUser(user);

        return JSON.toJSONString(access);
    }

    @RequestMapping(value = "/tokens", method = RequestMethod.DELETE)
    @ResponseBody
    public String logout() {
        String token = request.getHeader("X-Auth-Token");
        vmanager.expire(token);
        return "";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestBody User user) {

        logger.debug("register: request: " + user.toString());

        if (!userService.validUser(user)) {
            logger.debug("register: incorrect format");
            throw new BadRequestException("incorrect format");
        }

        try {
            userService.findUserByEmail(user.getEmail());
        } catch (NotFoundException notFound) {
            String validationCode = vmanager.generate(user);

            MailConfig config = SpringIocUtil.getBean("validateRegMailConfigure", MailConfig.class);
            config.setToAddress(user.getEmail());
            config.setValidationCode(validationCode);

            try {
                MailUtil.send(config);
            } catch (MessagingException e) {
                logger.error("register: sending mail failed");
                throw new UnknownException("sending mail failed." + e.getMessage());
            }

            return "";
        }
        logger.debug("register: user conflict");
        throw new ConflictException("user conflict");

    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public String validate() {

        String validationCode = request.getParameter("code");

        if (validationCode == null) {
            logger.debug("validation code null");
            throw new BadRequestException("no query parameter \'code\'.");
        }

        if (!vmanager.exists(validationCode)) {
            logger.debug("validate register: incorrect validation code");
            throw new exception.http.NotFoundException("incorrect validation code");
        }

        String userInfo = vmanager.getRegistrationInfo(validationCode);
        User newUser = JSON.parseObject(userInfo, User.class);

        try {
            userService.addUser(newUser);
        } catch (exception.service.ConflictException e) {
            logger.debug("validate register: user conflict, will not create");
        }
        vmanager.expire(validationCode);

        return "";
    }

}
