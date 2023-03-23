package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.ReportDog;

import java.util.Set;
/**
 * Интерфейс ReportDogRepository
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {
    /**
     * Метод получения списка у объекта "Пользователь, интересующийся котом" по чат-айди.
     * @param chatId
     */
    Set<ReportDog>findAllByUserDog_ChatId(Long chatId);
    /**
     * Метод получения объекта "Пользователь, интересующийся котом" по чат-айди.
     * @param chatId
     */
    Set<ReportDog>findByUserDog_ChatId(Long chatId);

}
