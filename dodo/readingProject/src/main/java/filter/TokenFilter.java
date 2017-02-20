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
import java.io.InputStream;

/**
 * Created by heming on 7/2/2016.
 */
@Component
@WebFilter(filterName = "TokenFilter", urlPatterns = "/*")
public class TokenFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

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

        String uri = request.getRequestURI();
        if (!((uri.endsWith("auth/tokens") && request.getMethod().equalsIgnoreCase("post")) || uri.endsWith("auth/register"))) {
            String token = request.getHeader("X-Auth-Token");
            TokenManager tokenManager = SpringIocUtil.getBean("tokenManager", TokenManager.class);
            if (token == null || token.equals("")) {
                logger.debug("filter: token is null");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not found in header");
            }
            else if (!tokenManager.exists(token)) {
                logger.debug("filter: token not exist");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in first");
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }

    public void init(FilterConfig config) throws ServletException {

    }

}
