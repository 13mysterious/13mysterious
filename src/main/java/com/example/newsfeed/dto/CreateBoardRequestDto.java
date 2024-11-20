package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class CreateBoardRequestDto {

    private final String title;

    private final String contents;

    public CreateBoardRequestDto(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}

