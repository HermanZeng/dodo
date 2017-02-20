package security.token;

import dao.UserDAO;
import entity.Role;
import entity.User;
import exception.dao.NotFoundException;
import exception.security.ExecuteException;
import exception.security.GenerationException;
import redis.clients.jedis.Jedis;
import security.SecurityCodeGenerator;
import security.SecurityManager;
import utilities.RedisUtil;

/**
 * Created by heming on 6/29/2016.
 */
public class TokenManager extends SecurityManager {
    private SecurityCodeGenerator generator;
    private UserDAO userDAO;
    private static final int adminRef = 1;
    private static final int vipRef = 2;

    @Override
    public String generate(Object object) throws GenerationException {

        User user = (User) object;
        String token = generator.generate(user);
        save(token, user);
        return token;
    }


    private void save(String token, User user) {
        Jedis jedis = RedisUtil.getJedis();
        jedis.set(token, user.getId());
        jedis.expire(token, 1200);
        RedisUtil.returnResource(jedis);
    }

    public boolean isAdmin(String token) throws ExecuteException {
        Jedis jedis = RedisUtil.getJedis();
        String user_id = jedis.get(token);

        try {
            User user = userDAO.find(user_id);
            for (Role role : user.getRoles()) {
                if (role.getReference() == adminRef) {
                    return true;
                }
            }
            return false;
        } catch (NotFoundException e) {
            throw new ExecuteException("User id not found in database");
        } finally {
            RedisUtil.returnResource(jedis);
        }
    }

    public boolean isVIP(String token) throws ExecuteException {
        Jedis jedis = RedisUtil.getJedis();
        String user_id = jedis.get(token);

        try {
            User user = userDAO.find(user_id);
            for (Role role : user.getRoles()) {
                if (role.getReference() == vipRef) {
                    return true;
                }
            }
            return false;
        } catch (NotFoundException e) {
            throw new ExecuteException("User id not found in database");
        } finally {
            RedisUtil.returnResource(jedis);

        }
    }

    public String getUserId(String token) {
        Jedis jedis = RedisUtil.getJedis();
        String userId = jedis.get(token);
        RedisUtil.returnResource(jedis);
        return userId;
    }

    public SecurityCodeGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(SecurityCodeGenerator generator) {
        this.generator = generator;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
