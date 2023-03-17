package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.AdopterDogNotFoundException;
import ru.team2.lookingforhouse.model.AdopterDog;
import ru.team2.lookingforhouse.repository.AdopterDogRepository;


import java.util.Collection;
/**
 * Класс сервиса объекта "Усыновитель собаки"
 *
 * @author Одокиенко Екатерина
 */
/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class AdopterDogService {

    /**
     * Поле создания слоя репозитория
     */
    private final AdopterDogRepository adopterDogRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see AdopterDogService#AdopterDogService(AdopterDogRepository adopterDogRepository)
     */

    public AdopterDogService(AdopterDogRepository adopterDogRepository) {
        this.adopterDogRepository = adopterDogRepository;
    }

    /**
     * Метод получения объекта "Усыновитель собаки" по чат-айди, который присваивается Телеграмм-ботом
     *
     * @param chatId
     * @return {@link AdopterDogRepository#findByChatId(Long)}
     * @see AdopterDogService
     */
    public Collection<AdopterDog> getByChatId(Long chatId) {
        log.info("Вы вызвали метод получения объекта \"Усыновитель собаки\" по chatId={}", chatId);
        return this.adopterDogRepository.findByChatId(chatId);
    }

    /**
     * Метод получения объекта "Усыновитель собаки" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link AdopterDogRepository#findById(Object)}
     * @throws AdopterDogNotFoundException, если объект "Усыновитель собаки" с указанным id не был найден в БД
     * @see AdopterDogService
     */
    public AdopterDog getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Усыновитель собаки\" по id={}", id);
        return this.adopterDogRepository.findById(id).orElseThrow(AdopterDogNotFoundException::new);
    }

    /**
     * Метод создания объекта "Усыновитель собаки"
     *
     * @param adopterDog
     * @return {@link AdopterDogRepository#save(Object)}
     * @see AdopterDogService
     */
    public AdopterDog create(AdopterDog adopterDog) {
        log.info("Вы вызвали метод создания объекта \"Усыновитель собаки\"");
        return this.adopterDogRepository.save(adopterDog);
    }

    /**
     * Метод редактирования объекта "Усыновитель кота"
     *
     * @param adopterDog
     * @return {@link AdopterDogRepository#save(Object)}
     * @throws AdopterDogNotFoundException(), если объект "Усыновитель собаки" с указанным id не был найден в БД
     * @see AdopterDogService
     */
    public AdopterDog update(AdopterDog adopterDog) {
        log.info("Вы вызвали метод редактирования объекта \"Усыновитель собаки\"");
        if (adopterDog.getId() != null && getById(adopterDog.getId()) != null) {
            return this.adopterDogRepository.save(adopterDog);
        }
        throw new AdopterDogNotFoundException();
    }

    /**
     * Метод получения списка всех пользователей у объекта "Усыновитель собаки"
     *
     * @return {@link AdopterDogRepository#findAll()}
     * @see AdopterDogService
     */
    public Collection<AdopterDog> getAll() {
        log.info("Вы вызвали метод получения всех пользователей у объекта \"Усыновитель собаки\"");
        return this.adopterDogRepository.findAll();
    }

    /**
     * Метод удаления объекта "Усыновитель собаки" по айди, который присваиваектся Базой Данных
     *
     * @param id
     * @return {@link AdopterDogRepository#deleteById(Object)}
     * @see AdopterDogService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Усыновитель собаки\" по id={}", id);
        this.adopterDogRepository.deleteById(id);
    }
}

