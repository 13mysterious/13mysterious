package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

    private final String title;

    private final String contents;

    public BoardCreateRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

