package com.example.newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardUpdateRequestDto {

    private final String title;

    private final String contents;
}
