package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.team2.lookingforhouse.model.ReportData;

import java.util.Set;
/**
 * Интерфейс ReportDataRepository.
 *
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
@Repository
public interface ReportDataRepository extends JpaRepository<ReportData, Long> {
    /**
     * Метод получения списка объектов у объекта "Отчет данных" по айди.
     * @param id
     */
    Set<ReportData> findListByChatId(Long id);

    /**
     * Метод получения объекта "Отчет о данных" по айди.
     * @param id
     */
    ReportData findByChatId(Long id);

}
