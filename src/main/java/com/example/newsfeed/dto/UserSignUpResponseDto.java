package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final LocalDate birth;

    private final int age;

    public UserSignUpResponseDto(Long id, String name, String email, LocalDate birth, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.age = age;
    }
}
