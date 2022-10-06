package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class UserAuthentication extends ActionSupport {
    public String login () throws IOException {
        SessionMap session = (SessionMap) ActionContext.getContext().getSession();
        Boolean isLogin = (Boolean) session.get("isLogin");
        if (Objects.equals(isLogin, true)) {
            HttpServletRequest request = ServletActionContext.getRequest();
            String redirectURL = request.getParameter("redirectURL");
            if (!StringUtils.isEmpty(redirectURL)) {
                HttpServletResponse response = ServletActionContext.getResponse();
                response.sendRedirect(redirectURL);
                return null;
            }
            return SUCCESS;
        }
        return INPUT;
    }

    public String logout() {
        SessionMap session = (SessionMap) ActionContext.getContext().getSession();
        session.clear();
        session.invalidate();
        return SUCCESS;
    }

    public String register() {
        return SUCCESS;
    }
}
