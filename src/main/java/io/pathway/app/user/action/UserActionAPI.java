package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
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

    public void listUser() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long organisationId = (Long) session.get("organisationId");
            List<User> users = UserService.listUsersInOrg(organisationId);
            result.put("success", true);
            result.put("results", new JSONArray(users));
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }

    public void getProfile() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long userId = (Long) session.get("userId");
            Long organisationId = (Long) session.get("organisationId");
            JSONObject data = UserService.getProfile(userId, organisationId);
            result.put("success", true);
            result.put("results", data);
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }

    public void createOrUpdate() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long organisationId = (Long) session.get("organisationId");
            JSONObject data;
            if (request.getMethod().equalsIgnoreCase("POST")) {
                User user = UserService.createUser(request, organisationId);
                data = new JSONObject(user);
            } else {
                User user = UserService.updateUser(request, organisationId);
                data = new JSONObject(user);
            }
            result.put("success", true);
            result.put("results", data);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}