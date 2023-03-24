package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.UserDogNotFoundException;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.UserDogRepository;

import java.util.Collection;
/**
 * Класс сервиса объекта "Пользователь, интересующийся собакой"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class UserDogService {

    /**
     * Поле создания слоя репозитория
     */
    private final UserDogRepository userDogRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see UserDogService#UserDogService(UserDogRepository userDogRepository)
     */

    public UserDogService(UserDogRepository userDogRepository) {
        this.userDogRepository = userDogRepository;
    }

    /**
     * Метод получения объекта "Пользователь, интересующийся собакой" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link UserDogRepository#findUserDogByChatId(Long)}
     * @see UserDogService
     */
    public UserDog findByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Пользователь, интересующийся собакой\" по chatId={}", chatId);
        return this.userDogRepository.findUserDogByChatId(chatId);
    }

    /**
     * Метод создания объекта "Пользователь, интересующийся собакой"
     *
     * @param userDog
     * @return {@link UserDogRepository#save(Object)}
     * @see UserDogService
     */
    public UserDog create(UserDog userDog) {
        log.info("Вы вызвали метод создания объекта \"Пользователь, интересующийся собакой\"");
        return this.userDogRepository.save(userDog);
    }

    /**
     * Метод редактирования объекта "Пользователь, интересующийся собакой"
     *
     * @param userDog
     * @return {@link UserDogRepository#save(Object)}
     * @throws UserDogNotFoundException, если объект "Пользователь, интересующийся собакой" с указанным id не был найден в БД
     * @see UserDogService
     */
    public UserDog update(UserDog userDog) {
        log.info("Вы вызвали метод редактирования объекта \"Пользователь, интересующийся собакой\"");
        if (userDog.getChatId() != null && findByChatId(userDog.getChatId()) != null) {
            return this.userDogRepository.save(userDog);
        }
        throw new UserDogNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Пользователь, интересующийся собакой"
     *
     * @return {@link UserDogRepository#findAll()}
     * @see UserDogService
     */
    public Collection<UserDog> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Пользователь, интересующийся собакой\"");
        return this.userDogRepository.findAll();
    }

    /**
     * Метод удаления объекта "Пользователь, интересующийся собакой" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link UserDogRepository#deleteById(Object)}
     * @see UserDogService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Пользователь, интересующийся собакой\" по id={}", id);
        this.userDogRepository.deleteById(id);
    }
}