package com.example.newsfeed.service;

import com.example.newsfeed.dto.FriendResponseDto;
import com.example.newsfeed.entity.Friend;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.service
 * <li>fileName       : FriendService
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 친구 신청 메서드. friend 테이블에 row 생성
     *
     * @param toUserId 친구 신청을 받는 유저 식별자
     * @param fromUserId 친구 신청을 보내는 유저 식별자
     */
    public void requestFriend(Long toUserId, Long fromUserId) {

        User findToUser = userRepository.findById(toUserId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        User findFromUser = userRepository.findById(fromUserId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Friend friend = new Friend(false, findToUser, findFromUser);
        friendRepository.save(friend);
    }

    /**
     * 선택한 유저의 전체 친구 조회
     *
     * @param id 유저 식별자
     * @return 유저의 친구 리스트
     */
    public List<FriendResponseDto> findAllFriends(Long id) {

        List<Friend> findFriends = friendRepository.findALlById(id);

        return findFriends.stream().map(FriendResponseDto::new).toList();
    }

    /**
     * 친구 수락 메서드. 본인의 친구창이 아닐 경우 예외 던짐
     *
     * @param friendId 친구 식별자
     * @param isLoginUser 로그인한 유저의 친구 목록이 맞다면 true
     */
    public void acceptFriend(Long friendId, boolean isLoginUser) {

        if(!isLoginUser) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        findFriend.changeIsAccepted(true);
    }

    public void deleteFriend(Long friendId, boolean isLoginUser) {

        if(!isLoginUser) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        friendRepository.delete(findFriend);
    }
}
