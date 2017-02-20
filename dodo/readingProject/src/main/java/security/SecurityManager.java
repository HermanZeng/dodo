package security;

import exception.security.GenerationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;
import utilities.RedisUtil;

/**
 * Created by fan on 7/2/2016.
 */
public abstract class SecurityManager {

    protected static final Logger logger = LogManager.getLogger();

    public abstract String generate(Object object) throws GenerationException;

    public boolean exists(String token) {
        Jedis jedis = RedisUtil.getJedis();
        if (jedis == null) {
            logger.error("SecurityManager: exists: RedisUtil.getJedis() returns null");
        }
        if (jedis.exists(token)) {
            jedis.expire(token, 1200);
            jedis.close();
            return true;
        } else {
            jedis.close();
            return false;
        }
    }

    public void expire(String token) {
        Jedis jedis = RedisUtil.getJedis();
        if (jedis == null) {
            logger.error("SecurityManager: expire: RedisUtil.getJedis() returns null");
        }
        try {
            jedis.del(token);
        } catch (Exception e) {
        } finally {
            jedis.close();
        }
    }
}
