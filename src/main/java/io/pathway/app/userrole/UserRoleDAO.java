package io.pathway.app.userrole;

import io.pathway.models.UserRole;
import io.pathway.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRoleDAO {
    public static List<UserRole> getUserRoleByOrganisationId(Long organisationId) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root).where(builder.equal(root.get("organisation"), organisationId));
        List<UserRole> userRole = session.createQuery(query).list();
        session.close();
        return userRole;
    }

    public static UserRole getUserRoleById(Long userRoleId, Long organisationId) throws Exception {
        Session session = HibernateUtil.getInstance().getSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        query.select(root).where(builder.and(
                builder.equal(root.get("id"), userRoleId),
                builder.equal(root.get("organisation"), organisationId)
        ));
        UserRole userRole = session.createQuery(query).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return userRole;
    }
}
