package com.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min=0, max=50,message = "제목은 50자 이내로 입력해주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요")
    private final String contents;

    public BoardCreateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

