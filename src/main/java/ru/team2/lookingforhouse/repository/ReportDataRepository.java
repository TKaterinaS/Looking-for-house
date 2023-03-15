package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ReportData;

public interface ReportDataRepository extends JpaRepository<ReportData,Long> {
}
