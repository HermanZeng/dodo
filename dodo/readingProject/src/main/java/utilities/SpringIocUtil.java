package utilities;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fan on 7/2/2016.
 */
public final class SpringIocUtil {
    private static ApplicationContext context = new ClassPathXmlApplicationContext("IocContext.xml");

    public static <BeanType> BeanType getBean(Class<BeanType> returnType) {
        return context.getBean(returnType);
    }

    public static <BeanType> BeanType getBean(String id, Class<BeanType> type) {
        return context.getBean(id, type);
    }

}
