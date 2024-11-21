package com.example.newsfeed.dto;

import com.example.newsfeed.entity.Friend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


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
