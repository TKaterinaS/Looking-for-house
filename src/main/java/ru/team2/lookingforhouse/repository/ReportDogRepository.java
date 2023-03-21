package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ReportDog;

public interface ReportDogRepository extends JpaRepository<ReportDog, Long> {
}
