package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/** Класс "User" (Пользователь)*/

/** Аннотация @Entity определяет, что класс может быть сопоставлен таблице.
 * И это все, это просто маркер, как, например, сериализуемый интерфейс. */
@Entity(name = "userDataTable")

/** Генерирует конструктор всех аргументов.
 * Конструктор, содержащий все аргументы, требует одного аргумента для каждого поля в классе. */
@AllArgsConstructor

/** Генерирует конструктор без параметров. */
@NoArgsConstructor

/** Сокращенная аннотация, которая объединяет функции
 * @ToString, @EqualsAndHashCode, @Getter/@Setter и @RequiredArgsConstructor вместе. */
@Data
public class User {

    /** Hibernate делегирует установку ID на уровень базы данных. */
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** Hibernate просмотрит имена и типы полей.*/
    @Id

    /** Поле "Идентификатор" пользователя, который присваивается Базой Данных */
    private Long id;

    /** Поле "Идентификатор" пользователя, который присваивается Телеграм-Ботом */
    private Long chatId;

    /** Поле "Имя"  */
    private  String firstName;

    /** Поле "Фамилия" */
    private  String lastName;

    /** Поле "Ник" пользователя  */
    private  String userName;

    /** Поле "Номер телефона" пользователя */
    private String telephone;

    /** Поле "Дата регистрации" пользователя */
    private Timestamp registeredAt;

   // /** Задает многозначную ассоциацию с кратностью "один ко многим". */
   // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)

   // /** Это задняя часть ссылки - она будет опущена при сериализации. */
   // @JsonBackReference

    ///** Поле "Список животных" */
   // private List<Animal> animals;

}
