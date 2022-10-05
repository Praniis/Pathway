package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import io.pathway.app.user.UserDAO;
import io.pathway.app.user.UserService;
import io.pathway.models.User;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthentication {
    public void processLogin() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = UserService.login(username, password);
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            session.put("isLogin", true);
            session.put("userId", user.getId());
            session.put("organisationId", user.getOrganisation().getId());
            session.put("userRoleId", user.getUserRole().getId());
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }

    public void processRegistration() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            User user = UserService.register(request);
            result.put("success", true);
            result.put("message", "User registered successfully");
            result.put("user", new JSONObject(user));
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }

    public void processChangePassword() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long userId = (Long) session.get("userId");
            User user = UserDAO.getUserById(userId);
            UserService.changePassword(user, oldPassword, newPassword);
            result.put("success", true);
            result.put("message", "Password changed successfully");
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}
