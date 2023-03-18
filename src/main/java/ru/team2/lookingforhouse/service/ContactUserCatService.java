package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ContactUserCatNotFoundException;
import ru.team2.lookingforhouse.model.ContactUserCat;
import ru.team2.lookingforhouse.repository.ContactUserCatRepository;


import java.util.Collection;
/**
 * Класс сервиса объекта "Контакты пользователя, интересующегося котом"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class ContactUserCatService {

    /**
     * Поле создания слоя репозитория
     */
    private final ContactUserCatRepository contactUserCatRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see ContactUserCatService#ContactUserCatService(ContactUserCatRepository contactUserCatRepository)
     */

    public ContactUserCatService(ContactUserCatRepository contactUserCatRepository) {
        this.contactUserCatRepository = contactUserCatRepository;
    }

    /**
     * Метод получения объекта "Контакты пользователя, интересующегося котом" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link ContactUserCatRepository#findByChatId(Long)}
     * @see ContactUserCatService
     */
    public Collection<ContactUserCat> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Контакты пользователя, интересующегося котом\" по chatId={}", chatId);
        return this.contactUserCatRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Контакты пользователя, интересующегося котом" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link ContactUserCatRepository#findById(Object)}
     * @throws ContactUserCatNotFoundException, если объект "Контакты пользователя, интересующегося котом" с указанным id не был найден в БД
     * @see ContactUserCatService
     */
    public ContactUserCat getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Контакты пользователя, интересующегося котом\" по id={}", id);
        return this.contactUserCatRepository.findById(id).orElseThrow(ContactUserCatNotFoundException::new);
    }

    /**
     * Метод создания объекта "Контакты пользователя, интересующегося котом"
     *
     * @param contactUserCat
     * @return {@link ContactUserCatRepository#save(Object)}
     * @see ContactUserCatService
     */
    public ContactUserCat create(ContactUserCat contactUserCat) {
        log.info("Вы вызвали метод создания объекта \"Контакты пользователя, интересующегося котом\"");
        return this.contactUserCatRepository.save(contactUserCat);
    }

    /**
     * Метод редактирования объекта "Контакты пользователя, интересующегося котом"
     *
     * @param contactUserCat
     * @return {@link ContactUserCatRepository#save(Object)}
     * @throws ContactUserCatNotFoundException(), если объект "Контакты пользователя, интересующегося котом" с указанным id не был найден в БД
     * @see ContactUserCatService
     */
    public ContactUserCat update(ContactUserCat contactUserCat) {
        log.info("Вы вызвали метод редактирования объекта \"Контакты пользователя, интересующегося котом\"");
        if (contactUserCat.getId() != null && getById(contactUserCat.getId()) != null) {
            return this.contactUserCatRepository.save(contactUserCat);
        }
        throw new ContactUserCatNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Контакты пользователя, интересующегося котом"
     *
     * @return {@link ContactUserCatRepository#findAll()}
     * @see ContactUserCatService
     */
    public Collection<ContactUserCat> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Контакты пользователя, интересующегося котом\"");
        return this.contactUserCatRepository.findAll();
    }

    /**
     * Метод удаления объекта "Контакты пользователя, интересующегося котом" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link ContactUserCatRepository#deleteById(Object)}
     * @see ContactUserCatService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Контакты пользователя, интересующегося котом\" по id={}", id);
        this.contactUserCatRepository.deleteById(id);
    }
}