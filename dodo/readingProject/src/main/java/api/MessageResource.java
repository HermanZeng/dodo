package api;

import com.alibaba.fastjson.JSON;
import entity.Message;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import exception.service.ExecuteException;
import exception.service.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.MessageService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 9/11/2016.
 */

@RestController
@RequestMapping(value = "/message")
public class MessageResource {
    private static final Logger logger = LogManager.getLogger();
    private static final MessageService messageService = SpringIocUtil.getBean("messageService", MessageService.class);
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listMessages() {
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        List<Message> messageList = null;

        try {
            messageList = messageService.listAllMessages(token);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("List messages error: " + ee.getMessage());
        }

        return JSON.toJSONString(messageList);
    }

    @RequestMapping(value = "/{message_id}", method = RequestMethod.HEAD)
    public void updateMessageStatus(@PathVariable String message_id) {
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            messageService.updateMessageStatus(Long.valueOf(message_id));
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            throw new exception.http.NotFoundException("Message ID error: no such message" + nfe.getMessage());
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("Update message status error: " + ee.getMessage());
        }

        return;
    }

    @RequestMapping(value = "/{message_id}", method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable String message_id) {
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        try {
            messageService.deleteMessage(Long.valueOf(message_id));
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            throw new exception.http.NotFoundException("Message ID error: no such message" + nfe.getMessage());
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("Delete message error: " + ee.getMessage());
        }

        return;
    }
}
