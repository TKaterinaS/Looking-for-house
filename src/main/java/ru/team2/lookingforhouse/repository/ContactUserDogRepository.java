package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.ContactUserDog;

import java.util.Set;
/**
 * Интерфейс ContactUserDogRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ContactUserDogRepository extends JpaRepository<ContactUserDog,Long> {
    /**
     * Метод получения объекта "Контакты пользователя, интересующегося собакой" по чат-айди.
     * @param chatId
     */
    Set<ContactUserDog> findByChatId(Long chatId);
}
