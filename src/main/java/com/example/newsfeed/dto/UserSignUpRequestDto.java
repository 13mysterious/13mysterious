package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpRequestDto {

    private final String name;

    private final String email;

    private final String password;

    private final LocalDate birth;

    private final int age;

    public UserSignUpRequestDto(String name, String email, String password, LocalDate birth, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.age = age;
    }
}
