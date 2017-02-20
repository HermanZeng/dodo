package dao;

import entity.Honor;
import exception.dao.ExecuteException;

import java.util.List;

/**
 * Created by heming on 9/9/2016.
 */
public interface HonorDAO {
    Honor save(Honor honor) throws ExecuteException;

    List<Honor> listHonor(String userId) throws ExecuteException;
}
