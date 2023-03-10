package ru.team2.lookingforhouse.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Class of dogs
 * autor Tokan Ekaterina
 */

/** Replace class with a table in the database */
@Entity(name = "userDataTable")

/** Generates an all-args constructor. */
@AllArgsConstructor

/** Generate a constructor with no parameters. */
@NoArgsConstructor

/** Convenient shortcut annotation that bundles the features of
 * @ToString, @EqualsAndHashCode, @Getter/@Setter
 * and @RequiredArgsConstructor together
 */
@Data
public class User {

    /** Hibernate delegates the installation of the ID to the database level */
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    /** Hibernate will look at the names and types of fields.*/
    @Id

    /** "ID" field*/
    private Long id;

    /** "First name" field */
    private  String firstName;

    /** "Last name" field */
    private  String lastName;

    /** "Username" field */
    private  String userName;

    /** "Telephone" field */
    private String telephone;

    /** "RegisteredAt" field */
    private Timestamp registeredAt;

    /** Specifies a many-valued association with one-to-many multiplicity.
     * When loading the parent entity, the child entity will not be loaded.
     * A proxy object will be created instead.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)

    /** Manages the reverse part of the reference and the fields/collections
     *  marked with this annotation are not serialised.
      */
    @JsonBackReference

    /** "List<Animal> animals" field */
    private List<Animal> animals;
}
