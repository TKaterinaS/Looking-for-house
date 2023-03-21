package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "ReportDog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDog {
    @Id
    private long id;
    private String infoMessage;
    private String photoId;
    @ManyToOne
    private UserCat userDog;
}
