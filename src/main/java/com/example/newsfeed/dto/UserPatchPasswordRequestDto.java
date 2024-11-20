package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class UserPatchPasswordRequestDto {
    private final String oldPassword;

    private final String newPassword;

    public UserPatchPasswordRequestDto(String oldPassword, String newPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
