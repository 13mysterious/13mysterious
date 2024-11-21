package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class UserUpdatePasswordRequestDto {

    private final String oldPassword;

    private final String newPassword;

    public UserUpdatePasswordRequestDto(String oldPassword, String newPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
