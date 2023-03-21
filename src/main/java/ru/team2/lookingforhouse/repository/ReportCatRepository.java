package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ReportCat;

import java.util.Set;
/**
 * Интерфейс ReportCatRepository
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {
}
