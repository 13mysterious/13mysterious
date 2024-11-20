package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class UserLoginResponseDto {

    private final Long id;

    private final String email;

    public UserLoginResponseDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
