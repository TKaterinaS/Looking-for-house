package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.UserCat;

import java.util.Optional;

/**
 * Интерфейс UserDogRepository.
 *
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository

public interface UserCatRepository extends JpaRepository<UserCat, Long> {
    /**
     * Метод получения объекта "Пользователь, интересующийся котом" по чат-айди.
     * @param chatId параметром является чат-айди пользователя
     *
     */
    UserCat findUserCatByChatId(Long chatId);
}
