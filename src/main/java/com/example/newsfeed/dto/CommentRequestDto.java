package com.example.newsfeed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 비울 수 없습니다.")
    @Size(max = 100, message = "댓글은 100자 이내로 작성해야 합니다.")
    private String contents;

    public CommentRequestDto() {
    }

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }


}
