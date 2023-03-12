package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.hibernate.HibernateSessionFactoryUtil;
import ru.team2.lookingforhouse.model.User;
import ru.team2.lookingforhouse.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
/**
 * Класс сервиса пользователя.
 * @author Одокиенко Екатерина
 */

/** Платформа для регистрации сообщений в Java */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class UserService {

    /** Поле создания слоя репозитория */
    private final UserRepository userRepository;

    /**
     * Конструктор - создание нового объекта.
     * @see UserService#UserService(UserRepository userRepository)
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод получения пользователя по чат-айди, который присваивается Телеграмм-ботом
     * @param chatId
     * @return возвращает пользователя, обернутого в {@link Optional}
     * @see UserService
     */
    public Optional<User> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения пользователя по chatId={}", chatId);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
//        return this.userRepository.getByChatId(chatId);
            return Optional.ofNullable(session.get(User.class, chatId));
        }
    }

    /**
     * Метод получения пользователя по айди, который присваивается Базой Данных
     * @param id
     * @return возвращает объект, обернутый в {@link Optional}
     * @see UserService
     */
    public Optional<User> getById(Long id) {
        log.info("Вы вызвали метод получения пользователя по id={}", id);
//        return this.userRepository.findById(id)
//                .orElseThrow(UserNotFoundException::new);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    /**
     * Метод создания пользователя
     * @param user
     * @return {@link UserRepository#save(Object)}
     * @see UserService
     */
    public User create(User user) {
        log.info("Вы вызвали метод создания пользователя");
//        return this.userRepository.save(user);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        return user;
    }

    /**
     * Метод редактирования пользователя
     * @param user
     * @return {@link UserRepository#save(Object)}
     * @see UserService
     */
    public User update(User user) {
        log.info("Вы вызвали метод редактирования пользователя");
//        if (user.getId() != null) {
//            if (getById(user.getId()) != null) {
//                return this.userRepository.save(user);
//            }
//        }
//        throw new UserNotFoundException();

        if (user.getId() != null) {
            if (getById(user.getId()).isPresent()) ;
        }
        EntityManager entityManager = HibernateSessionFactoryUtil.getSessionFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        User updated = entityManager.merge(user);
        entityTransaction.commit();
        return updated;
    }

    /**
     * Метод получения списка всех пользователей
     * @return {@link UserRepository#findAll()}
     * @see UserService
     */
    public List<User> getAll() {
        log.info("Вы вызвали метод получения всех пользователей");
//        return (List<User>) this.userRepository.findAll();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("From userDataTable", User.class).list();
        }
    }

    /**
     * Метод удаления пользователя по айди, который присваиваектся Базой Данных
     * @param user
     * @return возвращает объект, обернутый в {@link Optional}
     * @see UserService
     */
    public Optional<User> delete(User user) {
        log.info("Вы вызвали метод удаления пользователя");
//        this.userRepository.deleteById(id);
        Optional<User> optionalUser = getById(user.getId());
        if (optionalUser.isPresent()) {
            try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(optionalUser.get());
                transaction.commit();
                return optionalUser;
            }
        }
        return Optional.empty();
    }

}