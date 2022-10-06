package io.pathway.app.userrole;

import io.pathway.models.UserRole;

import java.util.List;

public class UserRoleService {
    public static List<UserRole> listUsersInOrg(Long organisationId) throws Exception {
        try {
            return UserRoleDAO.getUserRoleByOrganisationId(organisationId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to list userroles");
        }
    }
}
