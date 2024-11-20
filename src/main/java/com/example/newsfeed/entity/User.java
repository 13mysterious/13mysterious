package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private int age;

    @Column
    private LocalDate leaveDate;

    public User() {
    }

    public User(String name, String email, String password, LocalDate birth, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.age = age;
    }
}
