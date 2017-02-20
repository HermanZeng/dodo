package interceptor;

import java.util.Map;

import service.base.Connection;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginInterceptor extends AbstractInterceptor {
	private static final Logger logger = LogManager.getLogger();
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		ActionContext ctx = invocation.getInvocationContext();
		Map session = ctx.getSession();
		Connection conn = (Connection) session.get("connection");

		if (conn != null) {
			logger.debug("authorized.");
			return invocation.invoke();
		}

		logger.debug("not logged yet.");
		return Action.LOGIN;

	}

}
