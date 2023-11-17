package com.example.phase2.entity;

import com.example.phase2.entity.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "Users")
public abstract class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private LocalDateTime dateOfRegistration;
    @Lob
    @Column(name = "image")
    private byte[] image;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isEnabled;
    private String activationToken;
    private boolean activationTokenExpired;

    public User(String firstName, String lastName, String email, String password, Role role, Boolean isEnabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isEnabled = isEnabled;
    }
}
