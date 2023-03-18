package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.Report;

import java.util.Set;
/**
 * Интерфейс ReportRepository.
 *
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    /**
     * Метод получения списка объектов у объекта "Отчет данных" по чат-айди.
     * @param chatId
     */
    Set<Report> findListByChatId(Long chatId);

    /**
     * Метод получения объекта "Отчет о данных" по чат-айди.
     * @param chatId
     */
    Report findByChatId(Long chatId);

}
