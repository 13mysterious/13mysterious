package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserPatchInfoRequestDto {
    private String name;

    private final LocalDate birth;

    private final int age;

    public UserPatchInfoRequestDto(String name, LocalDate birth, int age) {
        this.name = name;
        this.birth = birth;
        this.age = age;
    }
}
