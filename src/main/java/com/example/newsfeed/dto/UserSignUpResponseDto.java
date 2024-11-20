package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpResponseDto {

    private final Long id;

    private final String email;

    private final String name;

    private final LocalDate birth;

    private final int age;

    public UserSignUpResponseDto(Long id, String email, String name, LocalDate birth, int age) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.age = age;
    }
}
