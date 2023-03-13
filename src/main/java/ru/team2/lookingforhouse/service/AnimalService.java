package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.AnimalNotFoundException;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.repository.AnimalRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Класс сервиса объекта "Животное"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class AnimalService {

    /**
     * Поле создания слоя репозитория
     */
    private final AnimalRepository animalRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see AnimalService#AnimalService(AnimalRepository animalRepository)
     */
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * Метод получения объекта "Животное" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link AnimalRepository#findById(Object)}
     * @throws AnimalNotFoundException
     * @see AnimalService
     */
    public Animal getById(Long id) {
        log.info("Вы вызвали метод получения объекта \"Животное\" по id={}", id);
        return this.animalRepository.findById(id).orElseThrow(AnimalNotFoundException::new);
    }

    /**
     * Метод создания объекта "Животное"
     *
     * @param animal
     * @return {@link AnimalRepository#save(Object)}
     * @see AnimalService
     */
    public Animal create(Animal animal) {
        log.info("Вы вызвали метод создания объекта \"Животное\"");
        return this.animalRepository.save(animal);
    }

    /**
     * Метод редактирования объекта "Животное"
     *
     * @param animal
     * @return {@link AnimalRepository#save(Object)}
     * @throws AnimalNotFoundException
     * @see AnimalService
     */
    public Animal update(Animal animal) {
        log.info("Вы вызвали метод редактирования объекта \"Животное\"");
        if (animal.getId() != null && getById(animal.getId()) != null) {
            return this.animalRepository.save(animal);
        }
        throw new AnimalNotFoundException();
    }

    /**
     * Метод получения списка всех животных у объекта "Животное"
     *
     * @return {@link AnimalRepository#findAll()}
     * @see AnimalService
     */
    public Collection<Animal> getAll() {
        log.info("Вы вызвали метод получения всех животных у объекта \"Животное\"");
        return this.animalRepository.findAll();
    }

    /**
     * Метод удаления объекта "Животное" по айди, который присваивается Базой Данных
     *
     * @param id
     * @return {@link AnimalRepository#deleteById(Object)}
     * @see UserService
     */
    public void deleteById(Long id) {
        log.info("Вы вызвали метод удаления объекта \"Животное\" по id={}", id);
        this.animalRepository.deleteById(id);
    }
}
