package com.example.newsfeed.dto;

import lombok.Getter;
import jakarta.validation.constraints.Pattern;

@Getter
public class UserLoginRequestDto {

    private final String email;

    private final String password;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "올바른 이메일 형식이 아닙니다.")

    public UserLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
