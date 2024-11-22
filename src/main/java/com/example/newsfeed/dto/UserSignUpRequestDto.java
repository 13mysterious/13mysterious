package com.example.newsfeed.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpRequestDto {

    private final String name;

    private final String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
    private final String password;

    private final LocalDate birth;

    private final int age;

    public UserSignUpRequestDto(String name, String email, String password, LocalDate birth, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.age = age;
    }
}
