package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import io.pathway.app.user.UserDAO;
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

public class UserActionAPI {

    public void getUsers() throws IOException {
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

    public void getProfile() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long userId = (Long) session.get("userId");
            JSONObject data = UserService.getProfile(userId);
            result.put("success", true);
            result.put("data", data);
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }

}