package ru.team2.lookingforhouse.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "contactUserDog")
public class ContactUserDog {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String registretAt;
    private String phone;
    private String email;
}
