package ru.team2.lookingforhouse.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.User;

import java.util.Optional;
/**
 * Интерфейс UserRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    /**
     * Метод получения пользователя по чат-айди.
     */
    Optional<User> getByChatId(Long chatId);
}
