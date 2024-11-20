package com.example.newsfeed.controller;

import com.example.newsfeed.dto.FriendResponseDto;
import com.example.newsfeed.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.controller
 * <li>fileName       : FriendController
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/friends")
public class FriendController {

    private final FriendService friendService;

    /**
     * 친구 신청 메서드
     *
     * @param userId     친구 신청을 받는 유저 식별자
     * @param fromUserId 친구 신청을 보내는 유저 식별자
     * @return 201
     */
    @PostMapping
    public ResponseEntity<Void> requestFriend(
            @PathVariable Long userId,
            @SessionAttribute(name = "userId") Long fromUserId
    ) {

        friendService.requestFriend(userId, fromUserId);

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
     * 친구 수락 메서드
     *
     * @param userId
     * @param friendId
     * @param loginUserId
     * @return
     */
    @PatchMapping("/{friendId}")
    public ResponseEntity<Void> acceptFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @SessionAttribute(name = "userId") Long loginUserId
    ) {

        friendService.acceptFriend(friendId, userId.equals(loginUserId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> deleteFriend(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @SessionAttribute(name = "userId") Long loginUserId
    ) {

        friendService.deleteFriend(friendId, userId.equals(loginUserId));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
