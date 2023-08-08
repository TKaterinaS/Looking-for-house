package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report_dog")
public class ReportDog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "info_message", length = 1024)
    private String infoMessage;
    @Column(name = "photo_id")
    private String photoId;
    @ManyToOne
    @JsonBackReference
    private UserDog userDog;
}
