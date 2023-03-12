package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.hibernate.HibernateSessionFactoryUtil;
import ru.team2.lookingforhouse.model.Dog;
import ru.team2.lookingforhouse.repository.DogRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
/**
 * Класс сервиса объекта "Собака".
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
     * Метод получения собаки по айди, который присваивается Базой Данных
     *
     * @param id
     * @return возвращает объект, обернутый в {@link Optional}
     * @see DogService
     */
    public Optional<Dog> getById(Long id) {
        log.info("Вы вызвали метод получения собаки по id={}", id);
//        return this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Dog.class, id));
        }
    }

    /**
     * Метод создания животного
     *
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @see DogService
     */
    public Dog create(Dog dog) {
        log.info("Вы вызвали метод создания собаки");
//        return this.dogRepository.save(dog);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(dog);
            transaction.commit();
        }
        return dog;
    }

    /**
     * Метод редактирования собаки
     *
     * @param dog
     * @return {@link DogRepository#save(Object)}
     * @see DogService
     */
    public Dog update(Dog dog) {
        log.info("Вы вызвали метод редактирования собаки");
//        if (dog.getId() != null) {
//            if (getById(dog.getId()) != null) {
//                return this.dogRepository.save(dog);
//            }
//        }
//        throw new DogNotFoundException();
        if (dog.getId() != null) {
            if (getById(dog.getId()).isPresent()) ;
        }
        EntityManager entityManager = HibernateSessionFactoryUtil.getSessionFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Dog updated = entityManager.merge(dog);
        entityTransaction.commit();
        return updated;
    }

    /**
     * Метод получения списка всех собак
     *
     * @return {@link DogRepository#findAll()}
     * @see DogService
     */
    public List<Dog> getAll() {
        log.info("Вы вызвали метод получения всех собак");
//        return this.dogRepository.findAll();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("From dogDataTable", Dog.class).list();
        }
    }

    /**
     * Метод удаления собаки по айди, который присваивается Базой Данных
     *
     * @param dog
     * @return возвращает объект, обернутый в {@link Optional}
     * @see DogService
     */
    public Optional<Dog> delete(Dog dog) {
        log.info("Вы вызвали метод удаления собаки");
//        this.dogRepository.deleteById(id);
        Optional<Dog> optionalDog = getById(dog.getId());
        if (optionalDog.isPresent()) {
            try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(optionalDog.get());
                transaction.commit();
                return optionalDog;
            }
        }
        return Optional.empty();
    }
}
