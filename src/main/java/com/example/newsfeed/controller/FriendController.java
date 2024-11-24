package com.example.newsfeed.controller;

import com.example.newsfeed.config.Const;
import com.example.newsfeed.dto.FriendResponseDto;
import com.example.newsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/friends")
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 신청
     *
     * @param userId     친구 신청을 받는 유저 식별자
     * @param sessionId 친구 신청을 보내는 유저 식별자
     * @return 201
     */
    @PostMapping
    public ResponseEntity<Void> createFriendRequest(
            @PathVariable Long userId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {

        friendService.createFriendRequest(userId, sessionId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 유저 친구 목록 조회
     *
     * @param userId 유저 식별자
     * @return 유저의 친구 목록
     */
    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> findAllFriends(
            @PathVariable Long userId
    ) {

        List<FriendResponseDto> allFriendsDto = friendService.findAllFriends(userId);

        return new ResponseEntity<>(allFriendsDto, HttpStatus.OK);
    }

    /**
     * 친구 수락
     *
     * @param userId      친구 요청 받은 유저 식별자
     * @param friendId    친구 요청한 유저 식별자
     * @param loginUserId 현재 로그인한 유저 식별자
     */
    @PatchMapping("/{friendId}")
    public ResponseEntity<Void> acceptFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {

        friendService.acceptFriend(friendId, userId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 친구 삭제/거절
     *
     * @param userId      대상 유저 식별자
     * @param friendId    친구 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     */
    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {

        friendService.deleteFriend(friendId, userId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
