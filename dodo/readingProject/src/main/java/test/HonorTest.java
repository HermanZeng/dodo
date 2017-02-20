package test;

import com.alibaba.fastjson.JSON;
import dao.impl.HibernateHonorDAO;
import service.HonorService;
import utilities.SpringIocUtil;

/**
 * Created by heming on 9/12/2016.
 */
public class HonorTest {
    private static final HonorService honorService = SpringIocUtil.getBean("honorService", HonorService.class);

    public static void main(String args[]) {
        HibernateHonorDAO honorDAO = new HibernateHonorDAO();
        System.out.println(JSON.toJSONString(honorService.listAllHonors("QjjVatbJCQBeMkEP/oaLEV3OXm0=")));
    }
}
