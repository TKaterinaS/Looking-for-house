package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.AdopterDog;

import java.util.Set;
/**
 * Интерфейс AdopterDogRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface AdopterDogRepository extends JpaRepository<AdopterDog,Long> {
    /**
     * Метод получения объекта "Усыновитель собаки" по чат-айди.
     * @param chatId
     */
    Set<AdopterDog> findByChatId(Long chatId);
}
