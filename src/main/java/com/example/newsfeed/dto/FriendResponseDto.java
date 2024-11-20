package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.dto
 * <li>fileName       : FriendResponseDto
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@Getter
@RequiredArgsConstructor
public class FriendResponseDto {

    private final Long id;

    private final String name;

    private final boolean isAccepted;

    public FriendResponseDto(Friend friend) {
        this.id = friend.getId();
        this.name = friend.getFromUser().getName();
        this.isAccepted = friend.isAccepted();
    }
}
