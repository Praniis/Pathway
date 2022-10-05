package io.pathway.app.user;

import io.pathway.models.User;
import io.pathway.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

public class UserDAO {

    public static void createUser(User user) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new Exception(e);
        }
    }

    public static void updateUser(User user) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new Exception(e);
        }
    }

    public static void deleteUserById(Long id) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getInstance().getSession();
            transaction = session.beginTransaction();
            User user = session.load(User.class, id);
            session.delete(user);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            throw new Exception(e);
        }
    }

    public static User getUserById(Long id) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("id"), id));
        return session.createQuery(query).uniqueResult();
    }

    public static User getUserByUserName(String username) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("username"), username));
        return session.createQuery(query).uniqueResult();
    }

    public static List<User> getUserByOrganisationId(Long id) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("organisation"), id));
        return session.createQuery(query).list();
    }
}
