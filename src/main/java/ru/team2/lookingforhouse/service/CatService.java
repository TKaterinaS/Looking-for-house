package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
//import ru.team2.lookingforhouse.exception.CatNotFoundException;
import ru.team2.lookingforhouse.hibernate.HibernateSessionFactoryUtil;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.repository.CatRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;

/**
 * Класс сервиса объекта "Кошка".
 *
 * @author Одокиенко Екатерина
 */

/** Платформа для регистрации сообщений в Java */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class CatService {
    /** Поле создания слоя репозитория */
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
     * Метод получения кошки по айди, который присваивается Базой Данных
     *
     * @param id
     * @return возвращает объект, обернутый в {@link Optional}
     * @see CatService
     */
    public Optional<Cat> getById(Long id) {
        log.info("Вы вызвали метод получения кошки по id={}", id);
//        return this.catRepository.findById(id).orElseThrow(CatNotFoundException::new);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Cat.class, id));
        }
    }

    /**
     * Метод создания кошки
     *
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @see CatService
     */
    public Cat create(Cat cat) {
        log.info("Вы вызвали метод создания кошки");
//        return this.catRepository.save(cat);
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(cat);
            transaction.commit();
        }
        return cat;
    }

    /**
     * Метод редактирования кошки
     *
     * @param cat
     * @return {@link CatRepository#save(Object)}
     * @see CatService
     */
    public Cat update(Cat cat) {
        log.info("Вы вызвали метод редактирования кошки");
//        if (cat.getId() != null) {
//            if (getById(cat.getId()) != null) {
//                return this.catRepository.save(cat);
//            }
//        }
//        throw new CatNotFoundException();
        if (cat.getId() != null) {
            if (getById(cat.getId()).isPresent()) ;
        }
        EntityManager entityManager = HibernateSessionFactoryUtil.getSessionFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Cat updated = entityManager.merge(cat);
        entityTransaction.commit();
        return updated;
    }

    /**
     * Метод получения списка всех кошек
     *
     * @return {@link CatRepository#findAll()}
     * @see CatService
     */
    public List<Cat> getAll() {
        log.info("Вы вызвали метод получения всех кошек");
//        return this.catRepository.findAll();
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("From catDataTable", Cat.class).list();
        }
    }
    /**
     * Метод удаления кошки по айди, который присваивается Базой Данных
     * @param cat
     * @return возвращает объект, обернутый в {@link Optional}
     * @see CatService
     */
    public Optional<Cat> delete(Cat cat) {
        log.info("Вы вызвали метод удаления кошки");
//        this.catRepository.deleteById(id);
        Optional<Cat> optionalCat = getById(cat.getId());
        if (optionalCat.isPresent()) {
            try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(optionalCat.get());
                transaction.commit();
                return optionalCat;
            }
        }
        return Optional.empty();
    }
}
