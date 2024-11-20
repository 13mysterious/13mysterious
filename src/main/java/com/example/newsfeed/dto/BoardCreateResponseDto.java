package com.example.newsfeed.dto;

import lombok.Getter;

@Getter
public class BoardCreateResponseDto {
    private final Long id;
    private final String title;
    private final String contents;

    public BoardCreateResponseDto(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }
}
