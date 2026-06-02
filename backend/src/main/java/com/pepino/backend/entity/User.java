package com.pepino.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "login")
    private String login;

    public User(UUID id) {
        this.id = id;
    }
}

