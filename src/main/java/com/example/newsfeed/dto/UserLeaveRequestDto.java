package com.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLeaveRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public UserLeaveRequestDto() {
    }

    public UserLeaveRequestDto(String password) {
        this.password = password;
    }
}
