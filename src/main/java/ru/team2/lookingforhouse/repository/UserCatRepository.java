package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.UserCat;

import java.util.Set;
/**
 * Интерфейс UserDogRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository

public interface UserCatRepository extends JpaRepository<UserCat,Long> {
    /**
     * Метод получения объекта "Пользователь, интересующийся котами" по чат-айди.
     * @param chatId
     */
    Set<UserCat> findByChatId(Long chatId);
}
