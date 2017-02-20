package filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import security.token.TokenManager;
import utilities.SpringIocUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by heming on 7/10/2016.
 */
@Component
@WebFilter(filterName = "AdminFilter", urlPatterns = "/admin/*")
public class AdminFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String reqMessage = "\n****request****";
        reqMessage += "\nfrom: " + request.getRemoteAddr();
        reqMessage += "\nX-Auth-Token: " + request.getHeader("X-Auth-Token");
        reqMessage += "\nurl: " + request.getRequestURL();
        reqMessage += "\nmethod: " + request.getMethod();
        reqMessage += "\n****************";
        logger.debug(reqMessage);

        String token = request.getHeader("X-Auth-Token");
        TokenManager tokenManager = SpringIocUtil.getBean("tokenManager", TokenManager.class);

        if (!tokenManager.isAdmin(token))
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not an administration");
        else
            chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
