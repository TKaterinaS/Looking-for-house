package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.TakenCat;

public interface TakenCatRepository extends JpaRepository<TakenCat,Long> {
}
