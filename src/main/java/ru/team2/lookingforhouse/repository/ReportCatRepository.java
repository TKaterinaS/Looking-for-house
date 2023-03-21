package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ReportCat;

public interface ReportCatRepository extends JpaRepository<ReportCat, Long> {
}
