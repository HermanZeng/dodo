package security.generateStrategy;

import exception.security.GenerationException;
import org.apache.commons.codec.binary.Base64;
import security.SecurityCodeGenerator;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;

/**
 * Created by fan on 7/2/2016.
 */
public class Sha1Generator implements SecurityCodeGenerator {
    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    private static final String encryptKey = "dodo_project";

    @Override
    public String generate(Object object) throws GenerationException {

        entity.User user = (entity.User) object;
        try {
            byte[] data = encryptKey.getBytes(ENCODING);
            SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
            Mac mac = Mac.getInstance(MAC_NAME);
            mac.init(secretKey);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String userData = user.getId() + user.getEmail() + timestamp;

            byte[] text = userData.getBytes(ENCODING);
            byte[] tokenBytes = mac.doFinal(text);
            String token = Base64.encodeBase64String(tokenBytes);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenerationException("Token generation error");
        }
    }
}
