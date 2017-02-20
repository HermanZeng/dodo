package service;

import dao.MessageDAO;
import dao.UserDAO;
import entity.Global;
import entity.Message;
import entity.User;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import exception.dao.NotFoundException;
import security.token.TokenManager;

import java.util.List;

/**
 * Created by heming on 9/4/2016.
 */
public class MessageService {
    private TokenManager tokenManager;
    private MessageDAO messageDAO;
    private UserDAO userDAO;

    public void addMessage(String userId, String msg, int type, String reqId) {
        User user = userDAO.find(userId);
        String userName = user.getFirstname();

        Message message = new Message();
        message.setMessage(userName + ", " + msg);
        message.setUserId(userId);
        message.setRead(Global.NotRead);
        message.setType(type);
        message.setReqId(reqId);

        try {
            messageDAO.save(message);
        } catch (IllegalInputException iie) {
            iie.printStackTrace();
            throw new exception.service.IllegalInputException("MessageDAO error: message cannot be read already" + iie.getMessage());
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("MessageDAO error: " + ee.getMessage());
        }

        return;
    }

    public void deleteMessage(long id) {
        try {
            messageDAO.delete(id);
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("MessageDAO error: message not found" + ne.getMessage());
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("MessageDAO error: execute error" + ee.getMessage());
        }

        return;
    }

    public void updateMessageStatus(long id) {
        Message message = null;

        try {
            message = messageDAO.find(id);
            message.setRead(Global.Read);
            messageDAO.update(id, message);
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            throw new exception.service.NotFoundException("MessageDAO error: message not found" + ne.getMessage());
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("MessageDAO error: execute error" + ee.getMessage());
        }

        return;
    }

    public List<Message> listAllMessages(String token) {
        String userId = tokenManager.getUserId(token);
        List<Message> messageList = null;

        try {
            messageList = messageDAO.findByUser(userId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("MessageDAO error: list all messages failed " + ee.getMessage());
        }

        return messageList;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public MessageDAO getMessageDAO() {
        return messageDAO;
    }

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
