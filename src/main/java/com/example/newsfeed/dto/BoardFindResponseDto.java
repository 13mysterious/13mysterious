package com.example.newsfeed.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardFindResponseDto {

    private final Long id;

    private final String title;

    private final String contents;

    private final String name;

    private final int likeCount;

    private final List comments;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public BoardFindResponseDto(
            Long id, String title, String contents,
            String name, int likeCount, List comments,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.likeCount = likeCount;
        this.comments = comments;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
