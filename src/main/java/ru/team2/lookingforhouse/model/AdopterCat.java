package ru.team2.lookingforhouse.model;


import javax.persistence.*;

@Entity(name = "takenCatTable")
public class AdopterCat {
    @Id
    private Long id;

    private String firstName;
    private String lastName;

    private String registretAt;

    private  String phone;

    private Long chatId;



}
