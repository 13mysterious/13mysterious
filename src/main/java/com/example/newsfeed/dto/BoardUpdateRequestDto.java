package com.example.newsfeed.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.dto
 * <li>fileName       : BoardUpdateRequestDto
 * <li>date           : 24. 11. 21.
 * <li>description    :
 * </ul>
 */

@Getter
@RequiredArgsConstructor
public class BoardUpdateRequestDto {

    private final String title;

    private final String contents;
}
