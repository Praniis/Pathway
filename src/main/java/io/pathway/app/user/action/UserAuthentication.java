package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.dispatcher.SessionMap;
import java.util.Objects;

public class UserAuthentication extends ActionSupport {
    public String login () {
        SessionMap session = (SessionMap) ActionContext.getContext().getSession();
        Boolean isLogin = (Boolean) session.get("isLogin");
        if (Objects.equals(isLogin, true)) {
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
