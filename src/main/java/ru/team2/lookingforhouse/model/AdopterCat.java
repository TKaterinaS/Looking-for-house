package ru.team2.lookingforhouse.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "AdopterCat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdopterCat {
    @Id
    private Long id;

    private String firstName;
    private String lastName;

    private String registretAt;

    private  String phone;

    private Long chatId;



}
