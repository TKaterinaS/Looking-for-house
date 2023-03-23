package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.ReportCat;

import java.util.Set;
/**
 * Интерфейс ReportCatRepository
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {
    /**
     * Метод получения списка у объекта "Пользователь, интересующийся котом" по чат-айди.
     * @param chatId
     */
    Set<ReportCat>findAllByUserCat_ChatId(Long chatId);
    /**
     * Метод получения объекта "Пользователь, интересующийся котом" по чат-айди.
     * @param chatId
     */
    Set<ReportCat>findByUserCat_ChatId(Long chatId);

}
