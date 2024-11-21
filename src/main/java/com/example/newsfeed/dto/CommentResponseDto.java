package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.dto
 * <li>fileName       : CommentResponseDto
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {

    private final Long id;

    private final String author;

    private final String contents;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getUser().getName();
        this.contents = comment.getContents();
    }
}
