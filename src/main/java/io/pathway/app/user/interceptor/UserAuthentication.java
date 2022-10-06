package io.pathway.app.user.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthentication implements Interceptor {
    public void destroy() {

    }

    public void init() {

    }

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        SessionMap session = (SessionMap) request.getSession();
        Boolean isLogin = (Boolean) session.get("isLogin");
        if (isLogin) {
            invocation.invoke();
            return null;
        } else if (request.getRequestURI().startsWith("/api")) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("error", "Not Authorized");
            response.getWriter().write(result.toString());
            return null;
        }
        return "login-required";
    }
}
