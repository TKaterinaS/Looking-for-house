package ru.team2.lookingforhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.team2.lookingforhouse.model.UserCat;

public interface UserCatRepository extends JpaRepository<UserCat,Long> {
}
