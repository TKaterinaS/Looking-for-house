package ru.team2.lookingforhouse.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.model.Dog;
import ru.team2.lookingforhouse.model.User;

/**
 * Класс, необходимый для того,чтобы создавать для нашего приложения фабрику сессий для работы с БД
 * @author Одокиенко Екатерина
 */
public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Cat.class);
                configuration.addAnnotatedClass(Dog.class);
                configuration.addAnnotatedClass(Animal.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}

