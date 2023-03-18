package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "UserCat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCat {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Timestamp registeredAt;
    private String userStatus;

}
