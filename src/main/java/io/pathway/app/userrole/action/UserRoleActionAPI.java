package io.pathway.app.userrole.action;

import com.opensymphony.xwork2.ActionContext;
import io.pathway.app.userrole.UserRoleService;
import io.pathway.models.UserRole;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserRoleActionAPI {
    public void listUserRole() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        JSONObject result = new JSONObject();
        try {
            SessionMap session = (SessionMap) ActionContext.getContext().getSession();
            Long organisationId = (Long) session.get("organisationId");
            List<UserRole> userroles = UserRoleService.listUsersInOrg(organisationId);
            result.put("success", true);
            result.put("results", new JSONArray(userroles));
        } catch (Exception e) {
            result.put("success", false);
            String errMsg = e.getMessage();
            result.put("error", errMsg);
        }
        response.setContentType("application/json");
        response.getWriter().write(result.toString());
    }
}
