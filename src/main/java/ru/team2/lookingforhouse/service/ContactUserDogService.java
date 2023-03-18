package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ContactUserDogNotFoundException;
import ru.team2.lookingforhouse.model.ContactUserDog;
import ru.team2.lookingforhouse.repository.ContactUserDogRepository;

import java.util.Collection;
/**
 * Класс сервиса объекта "Контакты пользователя, интересующегося собакой"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class ContactUserDogService {

    /**
     * Поле создания слоя репозитория
     */
    private final ContactUserDogRepository contactUserDogRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see ContactUserDogService#ContactUserDogService(ContactUserDogRepository contactUserDogRepository)
     */

    public ContactUserDogService(ContactUserDogRepository contactUserDogRepository) {
        this.contactUserDogRepository = contactUserDogRepository;
    }

    /**
     * Метод получения объекта "Контакты пользователя, интересующегося собакой" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link ContactUserDogRepository#findByChatId(Long)}
     * @see ContactUserDogService
     */
    public Collection<ContactUserDog> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Контакты пользователя, интересующегося собакой\" по chatId={}", chatId);
        return this.contactUserDogRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Контакты пользователя, интересующегося собакой" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link ContactUserDogRepository#findById(Object)}
     * @throws ContactUserDogNotFoundException, если объект "Контакты пользователя, интересующегося собакой" с указанным id не был найден в БД
     * @see ContactUserDogService
     */
    public ContactUserDog getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Контакты пользователя, интересующегося собакой\" по id={}", id);
        return this.contactUserDogRepository.findById(id).orElseThrow(ContactUserDogNotFoundException::new);
    }

    /**
     * Метод создания объекта "Контакты пользователя, интересующегося собакой"
     *
     * @param contactUserDog
     * @return {@link ContactUserDogRepository#save(Object)}
     * @see ContactUserDogService
     */
    public ContactUserDog create(ContactUserDog contactUserDog) {
        log.info("Вы вызвали метод создания объекта \"Контакты пользователя, интересующегося собакой\"");
        return this.contactUserDogRepository.save(contactUserDog);
    }

    /**
     * Метод редактирования объекта "Контакты пользователя, интересующегося собакой"
     *
     * @param contactUserDog
     * @return {@link ContactUserDogRepository#save(Object)}
     * @throws ContactUserDogNotFoundException(), если объект "Контакты пользователя, интересующегося собакой" с указанным id не был найден в БД
     * @see ContactUserDogService
     */
    public ContactUserDog update(ContactUserDog contactUserDog) {
        log.info("Вы вызвали метод редактирования объекта \"Контакты пользователя, интересующегося собакой\"");
        if (contactUserDog.getId() != null && getById(contactUserDog.getId()) != null) {
            return this.contactUserDogRepository.save(contactUserDog);
        }
        throw new ContactUserDogNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Контакты пользователя, интересующегося собакой"
     *
     * @return {@link ContactUserDogRepository#findAll()}
     * @see ContactUserDogService
     */
    public Collection<ContactUserDog> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Контакты пользователя, интересующегося собакой\"");
        return this.contactUserDogRepository.findAll();
    }

    /**
     * Метод удаления объекта "Контакты пользователя, интересующегося собакой" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link ContactUserDogRepository#deleteById(Object)}
     * @see ContactUserDogService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Контакты пользователя, интересующегося собакой\" по id={}", id);
        this.contactUserDogRepository.deleteById(id);
    }
}