package com.example.phase2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@SuperBuilder
@Table(name = "Users")
public abstract class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    String firstName;
    String lastName;
    @Column(unique = true)
    String email;
    @Column(length = 8)
    String password;
    Date dateOfRegistration;
    @Lob
    @Column(name = "image")
    private byte[] image;
    int credit;

    public User(String firstName, String lastName, String email) {
    }

//    public User(String firstName, String lastName, String email, byte[] image) throws IOException {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        if (Validator.isValidEmail(email))
//            this.email = email;
//        else
//            throw new RuntimeException("email is not valid!");
//        this.password = PasswordGenerator.generatePassword();
//        this.dateOfRegistration = Date.valueOf(LocalDate.now());
//        this.credit = 0;
//        this.image = image;
//
//    }
//
//    public User(String firstName, String lastName, String email) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        if (Validator.isValidEmail(email))
//            this.email = email;
//        else
//            throw new RuntimeException("email is not valid!");
//        this.password = PasswordGenerator.generatePassword();
//        this.dateOfRegistration = Date.valueOf(LocalDate.now());
//        this.credit = 0;
//    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
