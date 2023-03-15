package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "UserCat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCat {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int yearOfBirth;

    private String phone;

    private String mail;

    private String address;

    private Long chatId;
}
