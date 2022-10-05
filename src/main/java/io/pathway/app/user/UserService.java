package io.pathway.app.user;

import com.password4j.Password;
import com.sun.jdi.event.ExceptionEvent;
import io.pathway.models.Organisation;
import io.pathway.models.User;
import io.pathway.models.UserRole;
import io.pathway.util.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

public class UserService {

    public static User login(String username, String password) throws Exception {
        User user = null;
        try {
            user = UserDAO.getUserByUserName(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to login");
        }
        if (Objects.nonNull(user)) {
            if (UserService.checkPassword(user.getPassword(), password)) {
                return user;
            }
        }
        throw new Exception("Invalid username/password");
    }

    public static User register(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String orgName = request.getParameter("orgName");
        String orgAddress = request.getParameter("orgAddress");
        String orgEmail = request.getParameter("orgEmail");
        String orgWebsite = request.getParameter("orgWebsite");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(orgName)) {
            throw new Exception("Input all necessary fields");
        }

        User existingUser = UserDAO.getUserByUserName(username);
        if (Objects.nonNull(existingUser)) {
            throw new Exception("Username already exists.");
        }

        String passwordHash = Password.hash(password).withBcrypt().getResult();
        Organisation organisation = new Organisation();
        organisation.setName(orgName);
        organisation.setAddress(orgAddress);
        organisation.setEmail(orgEmail);
        organisation.setWebsite(orgWebsite);

        UserRole userrole = UserService.getDefaultUserRole();
        userrole.setOrganisation(organisation);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordHash);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setIsActive(true);
        user.setOrganisation(organisation);
        user.setUserRole(userrole);

        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            session.save(organisation);
            session.save(userrole);
            session.save(user);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            session.close();
            e.printStackTrace();
            throw new Exception("Unable to register");
        }
        return user;
    }

    private static boolean checkPassword(String hash, String password) {
        return Password.check(password, hash).withBcrypt();
    }

    private static UserRole getDefaultUserRole() {
        JSONObject permission = new JSONObject();
        permission.put("name", "Default");

        UserRole userrole = new UserRole();
        userrole.setName("Administrator");
        userrole.setPermission(permission.toString());
        return userrole;
    }

    public static void changePassword(User user, String oldPassword, String newPassword) throws Exception {
        if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new Exception("Input all necessary fields");
        }

        if (!UserService.checkPassword(user.getPassword(), oldPassword)) {
            throw new Exception("Incorrect old password");
        }

        try {
            String passwordHash = Password.hash(newPassword).withBcrypt().getResult();
            user.setPassword(passwordHash);
            UserDAO.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to change password");
        }
    }

    public static List<User> listUsersInOrg(Long orgId) throws Exception {
        try {
            List<User> users = UserDAO.getUserByOrganisationId(orgId);
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to list users");
        }
    }
}
