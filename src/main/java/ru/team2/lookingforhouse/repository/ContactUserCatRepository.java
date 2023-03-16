package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.ContactUserCat;


public interface ContactUserCatRepository extends JpaRepository<ContactUserCat,Long> {
}
