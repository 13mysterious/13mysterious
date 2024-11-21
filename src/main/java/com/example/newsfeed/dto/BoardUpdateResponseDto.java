package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Board;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardUpdateResponseDto {

    private final Long id;

    private final String title;

    private final String contents;

    public BoardUpdateResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents = board.getContents();
    }
}
