package com.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserUpdateInfoRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10글자 이하로 입력해주세요")
    private final String name;

    @NotNull(message = "생일을 입력해주세요.")
    private final LocalDate birth;

    @NotNull(message = "나이을 입력해주세요.")
    private final int age;

    public UserUpdateInfoRequestDto(String name, LocalDate birth, int age) {
        this.name = name;
        this.birth = birth;
        this.age = age;
    }
}
