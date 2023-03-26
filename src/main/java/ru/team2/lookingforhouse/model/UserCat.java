package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.team2.lookingforhouse.util.StatusAttributeConverter;
import ru.team2.lookingforhouse.util.UserStatus;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_cat")
public class UserCat {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "first_name", length = 25)
    private String firstName;
    @Column(name = "last_name", length = 25)
    private String lastName;
    @Column(name = "user_name", length = 25)
    private String userName;
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
    @Column(name = "registered_at")
    @CreationTimestamp
    private LocalDateTime registeredAt;
    @Convert(converter = StatusAttributeConverter.class)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userCat")
    @JsonManagedReference
    List<ReportCat> reports;

}
