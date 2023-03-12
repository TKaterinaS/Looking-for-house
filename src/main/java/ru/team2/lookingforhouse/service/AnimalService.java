package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.hibernate.HibernateSessionFactoryUtil;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.repository.AnimalRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Класс сервиса животного.
 * @author Одокиенко Екатерина
 */

/** Платформа для регистрации сообщений в Java */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class AnimalService {

    /** Поле создания слоя репозитория */
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
     * Метод получения животного по айди, который присваивается Базой Данных
     * @param id
     * @return возвращает объект, обернутый в {@link Optional}
     * @see UserService
     */
    public Optional<Animal> getById(Long id) {
        log.info("Вы вызвали метод получения животного по id={}", id);
//        return this.animalRepository.findById(id).orElseThrow(AnimalNotFoundException::new);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Animal.class, id));
        }
    }

    /**
     * Метод создания животного
     * @param animal
     * @return {@link AnimalRepository#save(Object)}
     * @see AnimalService
     */
    public Animal create(Animal animal) {
        log.info("Вы вызвали метод создания животного");
//        return this.animalRepository.save(animal);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(animal);
            transaction.commit();
        }
        return animal;
    }

    /**
     * Метод редактирования животного
     * @param animal
     * @return {@link AnimalRepository#save(Object)}
     * @see AnimalService
     */
    public Animal update(Animal animal) {
        log.info("Вы вызвали метод редактирования животного");
//        if (animal.getId() != null) {
//            if (getById(animal.getId()) != null) {
//                return this.animalRepository.save(animal);
//            }
//        }
//        throw new AnimalNotFoundException();
        if (animal.getId() != null) {
            if (getById(animal.getId()).isPresent()) ;
        }
        EntityManager entityManager = HibernateSessionFactoryUtil.getSessionFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Animal updated = entityManager.merge(animal);
        entityTransaction.commit();
        return updated;
    }

    /**
     * Метод получения списка всех животных
     * @return {@link AnimalRepository#findAll()}
     * @see AnimalService
     */
    public List<Animal> getAll() {
        log.info("Вы вызвали метод получения всех животных");
//        return this.animalRepository.findAll();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("From animalDataTable", Animal.class).list();
        }
    }

    /**
     * Метод удаления пользователя по айди, который присваивается Базой Данных
     *
     * @param animal
     * @return возвращает объект, обернутый в {@link Optional}
     * @see UserService
     */
    public Optional<Animal> delete(Animal animal) {
        log.info("Вы вызвали метод удаления животного");
//        this.animalRepository.deleteById(id);
        Optional<Animal> optionalAnimal = getById(animal.getId());
        if (optionalAnimal.isPresent()) {
            try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(optionalAnimal.get());
                transaction.commit();
                return optionalAnimal;
            }
        }
        return Optional.empty();
    }
}
