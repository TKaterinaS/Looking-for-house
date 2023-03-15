package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.AdopterDog;

public interface TakenDogRepository extends JpaRepository<AdopterDog,Long> {
}
