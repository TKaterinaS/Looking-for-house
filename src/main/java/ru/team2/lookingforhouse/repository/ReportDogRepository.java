package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ReportDog;

import java.util.Set;
/**
 * Интерфейс ReportDogRepository
 * @author Одокиенко Екатерина
 */

/** Механизм для хранения, извлечения, обновления, удаления и поиска объектов. */
public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {


}
