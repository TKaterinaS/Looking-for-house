package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.ContactUserCat;

import java.util.Set;

/**
 * Интерфейс ContactUserCatRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ContactUserCatRepository extends JpaRepository<ContactUserCat,Long> {
    /**
     * Метод получения объекта "Контакты пользователя, интересующегося котом" по чат-айди.
     * @param chatId
     */
    Set<ContactUserCat> findByChatId(Long chatId);
}
