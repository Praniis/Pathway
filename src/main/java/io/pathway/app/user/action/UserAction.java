package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import io.pathway.app.user.UserService;
import io.pathway.models.User;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UserAction extends ActionSupport {

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

    public String home() {
        return SUCCESS;
    }

    public void getUsers() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long organisationId = (Long) session.get("organisationId");
            List<User> users = UserService.listUsersInOrg(organisationId);
            result.put("success", true);
            result.put("users", new JSONArray(users));
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}