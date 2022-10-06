package io.pathway.app.user.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

public class UserAuthentication implements Interceptor {
    public void destroy() {

    }

    public void init() {

    }

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        Map session = ActionContext.getContext().getSession();
        Boolean isLogin = (Boolean) session.get("isLogin");
        if (Objects.equals(isLogin, true)) {
            invocation.invoke();
        } else if (request.getRequestURI().startsWith("/api")) {
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("error", "Not Authorized");
            response.setContentType("application/json");
            response.getWriter().write(result.toString());
        } else {
            String redirectURL = request.getRequestURI();
            if (redirectURL.equals("/")) {
                response.sendRedirect("/login" );
            } else {
                response.sendRedirect("/login?redirectURL=" + redirectURL);
            }
        }
        return null;
    }
}
