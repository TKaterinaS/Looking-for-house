package ru.team2.lookingforhouse.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "takenDogTable")
public class TakenDog {
    @Id
    private Long id;

    private String userName;
    @OneToOne
    private Dog dog;
}
