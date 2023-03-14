package ru.team2.lookingforhouse.model;


import javax.persistence.*;

@Entity(name = "takenCatTable")
public class TakenCat {
    @Id
    private Long id;

    private  String userName;
    @OneToOne
    private Cat cat;



}
