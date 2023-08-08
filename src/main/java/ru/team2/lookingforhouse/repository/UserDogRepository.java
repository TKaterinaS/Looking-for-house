package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.UserDog;

import java.util.Optional;
/**
 * Интерфейс UserDogRepository.
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface UserDogRepository extends JpaRepository<UserDog,Long> {

    /**
     * Метод получения объекта "Пользователь, интересующийся собакой" по чат-айди.
     * @param chatId
     */
    UserDog findUserDogByChatId(Long chatId);
}
