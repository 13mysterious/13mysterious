package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class CommentResponseDto {

    private final Long id;

    private final String author;

    private final String contents;

    private final int likeCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getUser().getName();
        this.contents = comment.getContents();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
