package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentWithDateResponseDto {

    private final Long id;

    private final String author;

    private final String contents;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;


    public CommentWithDateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getUser().getName();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
