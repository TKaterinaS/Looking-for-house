package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class of dogs
 * autor Tokan Ekaterina
 */

/** Replace class with a table in the database */
@Entity(name = "animalDataTable")

/** Generates an all-args constructor. */
@AllArgsConstructor

/** Generate a constructor with no parameters. */
@NoArgsConstructor

/** Convenient shortcut annotation that bundles the features of
 * @ToString, @EqualsAndHashCode, @Getter/@Setter
 * and @RequiredArgsConstructor together
 */
@Data

public class Animal {

	/** Hibernate will look at the names and types of fields.*/
	@Id

	/** "ID" field*/
	private Long id;

	/** "Name" field */
	private String name;

	/** "age" field */
	private int age;

	/** "gender" field */
	private String gender;

	/** "petType" field */
	private String petType;

	/** Specifies a single-valued association to another entity class
	 * that has many-to-one multiplicity. */
	@ManyToOne(fetch = FetchType.LAZY)

	/** Specifies a column for joining an entity association
	 * or element collection. */
	@JoinColumn(name = "user_id", nullable = false)

	/** Manages the forward part of the reference and the fields
	 * marked by this annotation are the ones that get Serialised */
	@JsonManagedReference

	/** "User user" field */
	private User user;
}
