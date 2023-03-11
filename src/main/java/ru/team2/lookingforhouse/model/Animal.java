package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** Класс "Animal" (Животное)*/

/** Аннотация @Entity определяет, что класс может быть сопоставлен таблице.
 * И это все, это просто маркер, как, например, сериализуемый интерфейс. */
@Entity(name = "animalDataTable")

/** Генерирует конструктор всех аргументов.
 * Конструктор, содержащий все аргументы, требует одного аргумента для каждого поля в классе. */
@AllArgsConstructor

/** Генерирует конструктор без параметров. */
@NoArgsConstructor

/** Сокращенная аннотация, которая объединяет функции
 * @ToString, @EqualsAndHashCode, @Getter/@Setter и @RequiredArgsConstructor вместе. */
@Data

public class Animal {

	/** Hibernate просмотрит имена и типы полей.*/
	@Id

	/** Поле "Идентификатор", который присваивается Базой Данных */
	private Long id;

	/** Поле "Имя" животного */
	private String name;

	/** Поле "Возраст" животного */
	private int age;

	/** Поле "Пол" животного */
	private String gender;

	/** Поле "Тип животного" (кошка/собака) */
	private String petType;

	/** Задает однозначную ассоциацию с другим классом сущностей,
	 *  который имеет множественность "многие к одному". */
	@ManyToOne(fetch = FetchType.LAZY)

	/** Задает столбец для присоединения к ассоциации объектов или коллекции элементов.
	 *  Если JoinColumn сама аннотация по умолчанию,
	 *  предполагается один объединенный столбец и применяются значения по умолчанию. */
	@JoinColumn(name = "user_id", nullable = false)

	/** Это прямая часть ссылки, которая обычно сериализуется. */
	@JsonManagedReference

	/** Поле "Пользователя" используещее ссылку на класс "User" ("Пользователь") */
	private User user;
}
