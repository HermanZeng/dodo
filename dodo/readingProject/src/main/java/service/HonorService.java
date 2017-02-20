package service;

import dao.HonorDAO;
import entity.Honor;
import exception.dao.ExecuteException;
import security.token.TokenManager;

import java.util.List;

/**
 * Created by heming on 9/9/2016.
 */
public class HonorService {
    private TokenManager tokenManager;
    private HonorDAO honorDAO;

    public void addHonor(String token, String trackId, String bookId, int stageSeq, String title) {
        String userId = tokenManager.getUserId(token);

        Honor honor = new Honor();
        honor.setUserId(userId);
        honor.setTrackId(trackId);
        honor.setBookId(bookId);
        honor.setStageSeq(stageSeq);
        honor.setTitle(title);

        try {
            honorDAO.save(honor);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("HonorService error: " + ee.getMessage());
        }

        return;
    }

    public List<Honor> listAllHonors(String token) {
        String userId = tokenManager.getUserId(token);
        List<Honor> honorList = null;

        try {
            honorList = honorDAO.listHonor(userId);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new exception.service.ExecuteException("HonorDAO error: list all honors failed " + ee.getMessage());
        }

        return honorList;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public HonorDAO getHonorDAO() {
        return honorDAO;
    }

    public void setHonorDAO(HonorDAO honorDAO) {
        this.honorDAO = honorDAO;
    }
}
