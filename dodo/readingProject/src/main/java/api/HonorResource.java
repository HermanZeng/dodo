package api;

import com.alibaba.fastjson.JSON;
import entity.Honor;
import exception.dao.ExecuteException;
import exception.http.UnauthorizedException;
import exception.http.UnknownException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.HonorService;
import utilities.SpringIocUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by heming on 9/12/2016.
 */

@RestController
@RequestMapping(value = "/honor")
public class HonorResource {
    private static final Logger logger = LogManager.getLogger();
    private static final HonorService honorService = SpringIocUtil.getBean("honorService", HonorService.class);

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listHonors() {
        String token = request.getHeader("X-Auth-Token");
        if (token == null) {
            throw new UnauthorizedException("void token");
        }

        List<Honor> honorList = null;

        try {
            honorList = honorService.listAllHonors(token);
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new UnknownException("List honors error: " + ee.getMessage());
        }

        return JSON.toJSONString(honorList);
    }
}
