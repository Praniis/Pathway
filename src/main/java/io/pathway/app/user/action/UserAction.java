package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import io.pathway.app.user.UserDAO;
import io.pathway.models.User;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAction extends ActionSupport {

    private User user = new User();

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String home() throws Exception {
        Long userId = (Long) ActionContext.getContext().getSession().get("userId");
        Long organisationId = (Long) ActionContext.getContext().getSession().get("organisationId");
        user = (User) UserDAO.getUserById(userId, organisationId);
        return SUCCESS;
    }

    public String listUsers() throws Exception {
        return SUCCESS;
    }

}