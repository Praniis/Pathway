package io.pathway.app.user.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import io.pathway.app.user.UserDAO;
import io.pathway.models.User;

public class UserAction extends ActionSupport {

    User user = (User) UserDAO.getUserById((Long) ActionContext.getContext().getSession().get("userId"));

    public UserAction() throws Exception {
    }

    public String home() throws Exception {
        return SUCCESS;
    }

    public String listUsers() throws Exception {
        return SUCCESS;
    }

}