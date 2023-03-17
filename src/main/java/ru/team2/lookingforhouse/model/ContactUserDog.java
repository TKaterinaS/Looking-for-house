package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ContactUserDog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUserDog {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String registretAt;
    private String phone;
    private String email;
    private Long chatId;
}
