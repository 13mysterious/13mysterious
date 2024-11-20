package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class UserLeaveRequestDto {

    private String password;

    public UserLeaveRequestDto() {
    }

    public UserLeaveRequestDto(String password) {
        this.password = password;
    }
}
