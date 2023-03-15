package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.UserDog;

public interface UserDogRepository extends JpaRepository<UserDog,Long> {
}
