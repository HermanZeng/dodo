package util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fan on 7/1/2016.
 */
public final class SpringIocUtil {
    private static final ApplicationContext context = new ClassPathXmlApplicationContext("IocContext.xml");

    public static <BeanType> BeanType getBean(Class<BeanType> returnType){
        return  context.getBean(returnType);
    }

    public static <BeanType> BeanType getBean(String id){
        return (BeanType) context.getBean(id);
    }
}
