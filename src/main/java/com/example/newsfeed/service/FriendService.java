package com.example.newsfeed.service;

import com.example.newsfeed.dto.FriendResponseDto;
import com.example.newsfeed.entity.Friend;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    /**
     * 친구 신청 메서드. friend 테이블에 row 생성
     *
     * @param toUserId   친구 신청을 받는 유저 식별자
     * @param fromUserId 친구 신청을 보내는 유저 식별자
     */
    public void createFriendRequest(Long toUserId, Long fromUserId) {

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

        User findUser = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        List<Friend> findFriends = friendRepository.findByToUser(findUser);

        return findFriends.stream().map(FriendResponseDto::new).toList();
    }

    /**
     * 친구 수락 메서드. 본인의 친구창이 아닐 경우 예외 던짐
     *
     * @param userId      현재 친구 목록을 가진 유저 식별자
     * @param loginUserId 현재 로그인한 유저 식별자
     */
    @Transactional
    public void acceptFriend(Long friendId, Long userId, Long loginUserId) {

        if (!userId.equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        findFriend.changeIsAccepted(true);
    }

    /**
     * 친구 삭제
     *
     * @param friendId    삭제할 친구 식별자
     * @param userId      현재 친구 목록을 가진 유저 식별자
     * @param loginUserId 현재 로그인한 유저 식별자
     */
    public void deleteFriend(Long friendId, Long userId, Long loginUserId) {

        if (!userId.equals(loginUserId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        friendRepository.delete(findFriend);
    }
}
