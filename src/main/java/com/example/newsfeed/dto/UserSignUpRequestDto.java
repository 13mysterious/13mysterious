package com.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserSignUpRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 10, message = "이름은 10글자 이하로 입력해주세요")
    private final String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "올바른 이메일 형식이 아닙니다.")
    private final String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야 합니다.")
    @Size(min = 8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
    private final String password;

    @NotNull(message = "생일을 입력해주세요.")
    private final LocalDate birth;

    @NotNull(message = "나이를 입력해주세요.")
    private final int age;

    public UserSignUpRequestDto(String name, String email, String password, LocalDate birth, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birth = birth;
        this.age = age;
    }
}
