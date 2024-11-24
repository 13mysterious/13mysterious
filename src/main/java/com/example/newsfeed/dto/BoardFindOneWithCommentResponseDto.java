package com.example.newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardFindOneWithCommentResponseDto {
    private final BoardResponseDto board;

    private final List comments;
}
