package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserUpdateInfoRequestDto {

    private final String name;

    private final LocalDate birth;

    private final int age;

    public UserUpdateInfoRequestDto(String name, LocalDate birth, int age) {
        this.name = name;
        this.birth = birth;
        this.age = age;
    }
}
