package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Board;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.dto
 * <li>fileName       : BoardUpdateResponseDto
 * <li>date           : 24. 11. 21.
 * <li>description    :
 * </ul>
 */

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
