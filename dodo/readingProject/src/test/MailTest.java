import utilities.SpringIocUtil;
import utilities.mail.MailConfig;
import utilities.mail.MailUtil;

import javax.mail.MessagingException;

/**
 * Created by heming on 7/2/2016.
 */
public class MailTest {
    public static void main(String[] args){
        MailConfig config = SpringIocUtil.getBean(MailConfig.class);
        config.setToAddress("972818442@qq.com");
        config.setValidationCode("13asdf43");
        try {
            MailUtil.send(config);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
