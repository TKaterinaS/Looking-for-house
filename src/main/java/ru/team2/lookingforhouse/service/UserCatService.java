package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.UserCatNotFoundException;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.repository.UserCatRepository;

import java.util.Collection;
/**
 * Класс сервиса объекта "Пользователь, интересующийся котом"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class UserCatService {

    /**
     * Поле создания слоя репозитория
     */
    private final UserCatRepository userCatRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see UserCatService#UserCatService(UserCatRepository userCatRepository)
     */

    public UserCatService(UserCatRepository userCatRepository) {
        this.userCatRepository = userCatRepository;
    }

    /**
     * Метод получения объекта "Пользователь, интересующийся котом" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link UserCatRepository#findByChatId(Long)}
     * @see UserCatService
     */
    public Collection<UserCat> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Пользователь, интересующийся котом\" по chatId={}", chatId);
        return this.userCatRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Пользователь, интересующийся котом" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link UserCatRepository#findById(Object)}
     * @throws UserCatNotFoundException, если объект "Пользователь, интересующийся котом" с указанным id не был найден в БД
     * @see UserCatService
     */
    public UserCat getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Пользователь, интересующийся котом\" по id={}", id);
        return this.userCatRepository.findById(id).orElseThrow(UserCatNotFoundException::new);
    }

    /**
     * Метод создания объекта "Пользователь, интересующийся котом"
     *
     * @param userCat
     * @return {@link UserCatRepository#save(Object)}
     * @see UserCatService
     */
    public UserCat create(UserCat userCat) {
        log.info("Вы вызвали метод создания объекта \"Пользователь, интересующийся котом\"");
        return this.userCatRepository.save(userCat);
    }

    /**
     * Метод редактирования объекта "Пользователь, интересующийся котом"
     *
     * @param userCat
     * @return {@link UserCatRepository#save(Object)}
     * @throws UserCatNotFoundException, если объект "Пользователь, интересующийся собакой" с указанным id не был найден в БД
     * @see UserCatService
     */
    public UserCat update(UserCat userCat) {
        log.info("Вы вызвали метод редактирования объекта \"Пользователь, интересующийся котом\"");
        if (userCat.getId() != null && getById(userCat.getId()) != null) {
            return this.userCatRepository.save(userCat);
        }
        throw new UserCatNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Пользователь, интересующийся котом"
     *
     * @return {@link UserCatRepository#findAll()}
     * @see UserCatService
     */
    public Collection<UserCat> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Пользователь, интересующийся котом\"");
        return this.userCatRepository.findAll();
    }

    /**
     * Метод удаления объекта "Пользователь, интересующийся котом" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link UserCatRepository#deleteById(Object)}
     * @see UserCatService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Пользователь, интересующийся котом\" по id={}", id);
        this.userCatRepository.deleteById(id);
    }
}