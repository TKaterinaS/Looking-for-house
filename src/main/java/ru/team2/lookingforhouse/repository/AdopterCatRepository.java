package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.AdopterCat;
import ru.team2.lookingforhouse.model.UserDog;

import java.util.Set;
/**
 * Интерфейс AdopterCatRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface AdopterCatRepository extends JpaRepository<AdopterCat,Long> {
    /**
     * Метод получения объекта "Усыновитель кота" по чат-айди.
     * @param chatId
     */
    Set<AdopterCat> findByChatId(Long chatId);
}
