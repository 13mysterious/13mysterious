package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpRequestDto {

    private final String email;

    private final String name;

    private final String password;

    private final LocalDate birth;

    private final int age;

    public UserSignUpRequestDto(String email, String name, String password, LocalDate birth, int age) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.age = age;
    }
}
