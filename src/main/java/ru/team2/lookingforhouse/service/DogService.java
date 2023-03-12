package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.DogNotFoundException;
import ru.team2.lookingforhouse.model.Dog;
import ru.team2.lookingforhouse.repository.DogRepository;

import java.util.Collection;

/**
 * Класс сервиса объекта "Собака"
 *
 * @author Одокиенко Екатерина
 */

/** Платформа для регистрации сообщений в Java */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class DogService {
    /** Поле создания слоя репозитория */
    private final DogRepository dogRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see DogService#DogService(DogRepository dogRepository)
     */
    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * Метод получения объекта "Собака" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link DogRepository#findById(Object)}
     * @see DogService
     * @exception  DogNotFoundException
     */
    public Dog getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Собака\" по id={}", id);
        return this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
    }

    /**
     * Метод создания объекта "Собака"
     *
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @see DogService
     */
    public Dog create(Dog dog) {
        log.info("Вы вызвали метод создания объекта \"Собака\"");
        return this.dogRepository.save(dog);
    }

    /**
     * Метод редактирования объекта "Собака"
     *
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @see DogService
     */
    public Dog update(Dog dog) {
        log.info("Вы вызвали метод редактирования объекта \"Собака\"");
        if (dog.getId() != null) {
            if (getById(dog.getId()) != null) {
                return this.dogRepository.save(dog);
            }
        }
        throw new DogNotFoundException();
    }

    /**
     * Метод получения списка всех собак у объекта "Собака"
     *
     * @return {@link DogRepository#findAll()}
     * @see DogService
     */
    public Collection<Dog> getAll() {
        log.info("Вы вызвали метод получения всех собак у объекта \"Собака\"");
        return this.dogRepository.findAll();
    }

    /**
     * Метод удаления объекта "Собака" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return  {@link DogRepository#deleteById(Object)}
     * @see DogService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Собака\" по id={}",id);
        this.dogRepository.deleteById(id);
    }
}
