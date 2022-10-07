package io.pathway.app.user;

import io.pathway.app.organisation.OrganisationDAO;
import io.pathway.app.userrole.UserRoleDAO;
import io.pathway.models.Organisation;
import io.pathway.models.User;
import io.pathway.models.UserRole;
import io.pathway.util.HibernateUtil;

import com.password4j.Password;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.json.JSONObject;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

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
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String orgName = request.getParameter("orgName");
        String orgAddress = request.getParameter("orgAddress");
        String orgEmail = request.getParameter("orgEmail");
        String orgWebsite = request.getParameter("orgWebsite");
        Long orgType = Long.valueOf(request.getParameter("orgType"));

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
        user.setName(name);
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
            return UserDAO.getUserByOrganisationId(orgId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to list users");
        }
    }

    public static JSONObject getProfile(Long userId, Long organisationId) throws Exception {
        try {
            JSONObject data = new JSONObject();
            User user = UserDAO.getUserById(userId, organisationId);
            UserRole userrole = user.getUserRole();
            Organisation organisation = user.getOrganisation();
            data.put("user", new JSONObject(user));
            data.put("userrole", new JSONObject(userrole));
            data.put("organisation", new JSONObject(organisation));
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to list users");
        }
    }

    public static User createUser(HttpServletRequest request, Long organisationId) throws Exception {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(request.getParameter("userRoleId"))) {
            throw new Exception("Input all necessary fields");
        }
        Long userRoleId = Long.valueOf(request.getParameter("userRoleId"));
        User existingUser = UserDAO.getUserByUserName(username);
        if (Objects.nonNull(existingUser)) {
            throw new Exception("Username already exists");
        }

        try {
            String hash = Password.hash(password).withBcrypt().getResult();
            Organisation organisation = OrganisationDAO.getOrganisationById(organisationId);
            UserRole userrole = UserRoleDAO.getUserRoleById(userRoleId, organisationId);

            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(hash);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setUserRole(userrole);
            user.setIsActive(true);
            user.setOrganisation(organisation);
            UserDAO.createUser(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to create user");
        }
    }

    public static User updateUser(HttpServletRequest request, Long organisationId) throws Exception {
        String requestData = request.getReader().lines().collect(Collectors.joining());
        HashMap<String, String> params = new HashMap<>();
        Arrays.stream(requestData.split("&")).forEach(param -> {
            String key = param.split("=")[0];
            String value = null;
            try {
                value = URLDecoder.decode(param.split("=")[1]);
            } catch (Exception e) {}
            params.put(key, value);
        });
        String id = params.get("id");
        String name = params.get("name");
        String username = params.get("username");
        String email = params.get("email");
        String mobile = params.get("mobile");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(id) || StringUtils.isEmpty(params.get("userRoleId"))) {
            throw new Exception("Input all necessary fields");
        }

        Long userRoleId = Long.valueOf(params.get("userRoleId"));
        User user = UserDAO.getUserById(Long.valueOf(id), organisationId);
        if (Objects.isNull(user)) {
            throw new Exception("User not found");
        } else if (!user.getUsername().equalsIgnoreCase(username)) {
            User existingUser = UserDAO.getUserByUserName(username);
            if (Objects.nonNull(existingUser)) {
                throw new Exception("Username already exists");
            }
        }

        try {
            UserRole userrole = UserRoleDAO.getUserRoleById(userRoleId, organisationId);
            user.setName(name);
            user.setUsername(username);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setUserRole(userrole);
            UserDAO.updateUser(user);
            return user;
        } catch (Exception e) {
            throw new Exception("Unable to update user");
        }
    }
}
