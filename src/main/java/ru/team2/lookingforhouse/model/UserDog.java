package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.team2.lookingforhouse.util.StatusAttributeConverter;
import ru.team2.lookingforhouse.util.UserStatus;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime registeredAt;

    @Convert(converter = StatusAttributeConverter.class)
    private UserStatus userStatus;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userDog")
    private List<ReportDog> reports;
}
