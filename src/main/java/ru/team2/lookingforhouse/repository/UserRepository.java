package ru.team2.lookingforhouse.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.User;

import java.util.Set;
/**
 * Интерфейс UserRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    /**
     * Метод получения объекта "Пользователь" по чат-айди.
     * @param chatId
     */
    Set<User> findByChatId(Long chatId);
}
