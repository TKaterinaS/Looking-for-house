package ru.team2.lookingforhouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** Класс "Dog" (Собака)*/

/** Аннотация @Entity определяет, что класс может быть сопоставлен таблице.
 * И это все, это просто маркер, как, например, сериализуемый интерфейс. */
@Entity

/** Генерирует конструктор всех аргументов.
 * Конструктор, содержащий все аргументы, требует одного аргумента для каждого поля в классе. */
@AllArgsConstructor

/** Генерирует конструктор без параметров. */
@NoArgsConstructor

/** Сокращенная аннотация, которая объединяет функции
 * @ToString, @EqualsAndHashCode, @Getter/@Setter и @RequiredArgsConstructor вместе. */
@Data
public class Dog {

	    /** Hibernate просмотрит имена и типы полей.*/
	    @Id

		/** Hibernate делегирует установку ID на уровень базы данных. */
		@GeneratedValue(strategy = GenerationType.IDENTITY)

		/** Поле "Идентификатор" животного, который присваивается Базой Данных */
		private Long id;

	    /** Поле "Порода" животного  */
		private String breed;

	    /** Поле "Имя" животного  */
		private String name;

	    /** Поле "Год рождения" животного  */
		private int yearOfBirth;

		/** Поле "Описание" */
		private String description;
}
