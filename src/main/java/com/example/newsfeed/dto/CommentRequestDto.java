package com.example.newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.dto
 * <li>fileName       : CommentRequestDto
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@Getter
public class CommentRequestDto {

    private String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }

    public CommentRequestDto(){}
}
