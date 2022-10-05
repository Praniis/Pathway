package io.pathway;

import io.pathway.models.User;
import io.pathway.util.HibernateUtil;
import org.hibernate.Session;

public class Main {
    public static void main(String[] args) {
        Session session = HibernateUtil.getInstance().getSession();
        User user = new User();
        user.setUsername("praniis-rest");
        user.setPassword("praniis");
        user.setIsActive(true);
        session.save(user);
        session.close();
    }
}
