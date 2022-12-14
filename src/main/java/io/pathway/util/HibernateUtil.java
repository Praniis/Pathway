package io.pathway.util;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

public class HibernateUtil {
    private static HibernateUtil instance = null;

    private HibernateUtil() {
    }

    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();
                MetadataSources sources = new MetadataSources(registry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public Session getSession() {
        Session session = getSessionFactory().getCurrentSession();
        if(Objects.isNull(session) || !session.isOpen()) {
            session = getSessionFactory().withOptions().openSession();
        }
        return session;
    }

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }
        return instance;
    }
}