package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.CatNotFoundException;
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.repository.CatRepository;

import java.util.Collection;

/**
 * Класс сервиса объекта "Кот".
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class CatService {
    /**
     * Поле создания слоя репозитория
     */
    private final CatRepository catRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see CatService#CatService(CatRepository catRepository)
     */
    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    /**
     * Метод получения объекта "Кот" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link CatRepository#findById(Object)}
     * @throws CatNotFoundException
     * @see CatService
     */
    public Cat getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Кот\" по id={}", id);
        return this.catRepository.findById(id).orElseThrow(CatNotFoundException::new);
    }

    /**
     * Метод создания объекта "Кот"
     *
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @see CatService
     */
    public Cat create(Cat cat) {
        log.info("Вы вызвали метод создания объекта \"Кот\"");
        return this.catRepository.save(cat);
    }

    /**
     * Метод редактирования объекта "Кот"
     *
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @throws CatNotFoundException
     * @see CatService
     */
    public Cat update(Cat cat) {
        log.info("Вы вызвали метод редактирования объекта \"Кот\"");
        if (cat.getId() != null) {
            if (getById(cat.getId()) != null) {
                return this.catRepository.save(cat);
            }
        }
        throw new CatNotFoundException();
    }

    /**
     * Метод получения списка всех котов у объекта "Кот"
     *
     * @return {@link CatRepository#findAll()}
     * @see CatService
     */
    public Collection<Cat> getAll() {
        log.info("Вы вызвали метод получения всех котов у объекта \"Кот\"");
        return this.catRepository.findAll();
    }

    /**
     * Метод удаления объекта "Кот" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link CatRepository#deleteById(Object)}
     * @see CatService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Кот\" по id={}", id);
        this.catRepository.deleteById(id);
    }
}
