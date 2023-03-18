package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.AdopterCatNotFoundException;
import ru.team2.lookingforhouse.model.AdopterCat;
import ru.team2.lookingforhouse.repository.AdopterCatRepository;


import java.util.Collection;
/**
 * Класс сервиса объекта "Усыновитель кота"
 *
 * @author Одокиенко Екатерина
 */
/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class AdopterCatService {

    /**
     * Поле создания слоя репозитория
     */
    private final AdopterCatRepository adopterCatRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see AdopterCatService#AdopterCatService(AdopterCatRepository adopterCatRepository)
     */

    public AdopterCatService(AdopterCatRepository adopterCatRepository) {
        this.adopterCatRepository = adopterCatRepository;
    }

    /**
     * Метод получения объекта "Усыновитель кота" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link AdopterCatRepository#findByChatId(Long)}
     * @see AdopterCatService
     */
    public Collection<AdopterCat> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Усыновитель кота\" по chatId={}", chatId);
        return this.adopterCatRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Усыновитель кота" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link AdopterCatRepository#findById(Object)}
     * @throws AdopterCatNotFoundException, если объект "Усыновитель кота" с указанным id не был найден в БД
     * @see AdopterCatService
     */
    public AdopterCat getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Усыновитель кота\" по id={}", id);
        return this.adopterCatRepository.findById(id).orElseThrow(AdopterCatNotFoundException::new);
    }

    /**
     * Метод создания объекта "Усыновитель кота"
     *
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @see AdopterCatService
     */
    public AdopterCat create(AdopterCat adopterCat) {
        log.info("Вы вызвали метод создания объекта \"Усыновитель кота\"");
        return this.adopterCatRepository.save(adopterCat);
    }

    /**
     * Метод редактирования объекта "Усыновитель кота"
     *
     * @param adopterCat
     * @return {@link AdopterCatRepository#save(Object)}
     * @throws AdopterCatNotFoundException(), если объект "Усыновитель кота" с указанным id не был найден в БД
     * @see AdopterCatService
     */
    public AdopterCat update(AdopterCat adopterCat) {
        log.info("Вы вызвали метод редактирования объекта \"Усыновитель кота\"");
        if (adopterCat.getId() != null && getById(adopterCat.getId()) != null) {
            return this.adopterCatRepository.save(adopterCat);
        }
        throw new AdopterCatNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Усыновитель кота"
     *
     * @return {@link AdopterCatRepository#findAll()}
     * @see AdopterCatService
     */
    public Collection<AdopterCat> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Усыновитель кота\"");
        return this.adopterCatRepository.findAll();
    }

    /**
     * Метод удаления объекта "Усыновитель кота" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link AdopterCatRepository#deleteById(Object)}
     * @see AdopterCatService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Усыновитель кота\" по id={}", id);
        this.adopterCatRepository.deleteById(id);
    }
}
