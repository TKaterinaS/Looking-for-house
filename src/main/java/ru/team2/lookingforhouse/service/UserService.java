package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.UserNotFoundException;
import ru.team2.lookingforhouse.model.User;
import ru.team2.lookingforhouse.repository.UserRepository;

import java.util.Collection;


/**
 * Класс сервиса объекта "Пользователь"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class UserService {

    /**
     * Поле создания слоя репозитория
     */
    private final UserRepository userRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see UserService#UserService(UserRepository userRepository)
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Метод получения объекта "Пользователь" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link UserRepository#findByChatId(Long)}
     * @see UserService
     */
    public Collection<User> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения пользователя по chatId={}", chatId);
        return this.userRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Пользователь" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link UserRepository#findById(Object)}
     * @see UserService
     */
    public User getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Пользователь\" по id={}", id);
        return this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Метод создания объекта "Пользователь"
     *
     * @param user
     * @return {@link UserRepository#save(Object)}
     * @see UserService
     */
    public User create(User user) {
        log.info("Вы вызвали метод создания объекта \"Пользователь\"");
        return this.userRepository.save(user);
    }

    /**
     * Метод редактирования объекта "Пользователь"
     *
     * @param user
     * @return {@link UserRepository#save(Object)}
     * @see UserService
     */
    public User update(User user) {
        log.info("Вы вызвали метод редактирования объекта \"Пользователь\"");
        if (user.getId() != null && getById(user.getId()) != null) {
            return this.userRepository.save(user);
        }
        throw new UserNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Пользователь"
     *
     * @return {@link UserRepository#findAll()}
     * @see UserService
     */
    public Collection<User> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Пользователь\"");
        return (Collection<User>) this.userRepository.findAll();
    }

    /**
     * Метод удаления объекта "Пользователь" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link UserRepository#deleteById(Object)}
     * @see UserService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Пользователь\"по id={}", id);
        this.userRepository.deleteById(id);
    }
}