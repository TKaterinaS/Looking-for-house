package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.team2.lookingforhouse.util.UserStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "UserDog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDog {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Timestamp registeredAt;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

}
