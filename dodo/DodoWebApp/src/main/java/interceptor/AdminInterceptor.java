package interceptor;

import java.util.Map;

import entity.Role;
import entity.User;
import service.base.Connection;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminInterceptor extends AbstractInterceptor {
	private static final Logger logger = LogManager.getLogger();
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext ctx = invocation.getInvocationContext();
		Map session = ctx.getSession();
		Connection conn = (Connection) session.get("connection");
        if (conn == null){
            return Action.LOGIN;
        }

		User user = conn.getUser();

        boolean isAdmin = false;
        for (Role role:user.getRoles()) {
            if (role.getReference()==1) isAdmin=true;
        }

		if (isAdmin) {
			logger.debug("admin authorized.");
			return invocation.invoke();
		}

		logger.debug("not an admin.");
		return Action.LOGIN;

	}
}
