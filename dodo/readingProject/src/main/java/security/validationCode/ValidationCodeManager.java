package security.validationCode;

import com.alibaba.fastjson.JSON;
import entity.User;
import exception.security.GenerationException;
import redis.clients.jedis.Jedis;
import security.SecurityCodeGenerator;
import security.SecurityManager;
import utilities.RedisUtil;

/**
 * Created by fan on 7/2/2016.
 */
public class ValidationCodeManager extends SecurityManager {
    private SecurityCodeGenerator generator;

    @Override
    public String generate(Object object) throws GenerationException {
        User user = (User) object;
        String validationCode = generator.generate(user);
        save(validationCode, user);
        return validationCode;
    }

    private void save(String validationCode, User user) {
        Jedis jedis = RedisUtil.getJedis();
        String json = JSON.toJSONString(user);
        jedis.set(validationCode, json);
        jedis.expire(validationCode, 1200);
        RedisUtil.returnResource(jedis);
    }

    public String getRegistrationInfo(String validationCode) {
        Jedis jedis = RedisUtil.getJedis();
        String json = jedis.get(validationCode);
        RedisUtil.returnResource(jedis);
        return json;
    }

    public SecurityCodeGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(SecurityCodeGenerator generator) {
        this.generator = generator;
    }
}
