package ru.team2.lookingforhouse.service;

import org.springframework.data.repository.CrudRepository;
import ru.team2.lookingforhouse.model.User;

public interface UserRepository extends CrudRepository<User,Long> {
}
