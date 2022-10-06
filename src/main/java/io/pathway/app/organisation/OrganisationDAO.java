package io.pathway.app.organisation;

import io.pathway.models.Organisation;
import io.pathway.models.UserRole;
import io.pathway.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class OrganisationDAO {
    public static Organisation getOrganisationById(Long organisationId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Organisation> query = builder.createQuery(Organisation.class);
        Root<Organisation> root = query.from(Organisation.class);
        query.select(root).where(builder.equal(root.get("id"), organisationId));
        Organisation organisation = session.createQuery(query).uniqueResult();
        session.close();
        return organisation;
    }
}
