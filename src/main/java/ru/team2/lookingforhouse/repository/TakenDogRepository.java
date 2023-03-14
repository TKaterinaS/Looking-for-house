package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.TakenCat;
import ru.team2.lookingforhouse.model.TakenDog;

public interface TakenDogRepository extends JpaRepository<TakenDog,Long> {
}
