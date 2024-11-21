package com.example.newsfeed.dto;

import lombok.Getter;


@Getter
public class CommentRequestDto {

    private String contents;

    public CommentRequestDto() {
    }

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }


}
