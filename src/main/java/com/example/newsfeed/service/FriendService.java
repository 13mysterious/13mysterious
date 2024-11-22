package com.example.newsfeed.service;

import com.example.newsfeed.dto.FriendResponseDto;
import com.example.newsfeed.entity.Friend;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


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
    @Transactional
    public void createFriendRequest(Long toUserId, Long fromUserId) {

        // 본인에게 친구신청 할 수 없음
        if(toUserId.equals(fromUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User findToUser = userRepository.findById(toUserId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND, "친구 요청을 보내려는 유저가 존재하지 않습니다.")
        );
        User findFromUser = userRepository.findById(fromUserId).orElseThrow(() ->
                new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Friend friend;
        Optional<Friend> reversedFriend = friendRepository.findByToUserAndFromUser(findFromUser, findToUser);
        // toUser와 fromUser가 반대인 친구 요청이 와있는 경우 양쪽을 true로 저장
        if(reversedFriend.isPresent()) {
            friend = new Friend(true, findToUser, findFromUser);
            reversedFriend.get().changeIsAccepted(true);
        } else {
            friend = new Friend(false, findToUser, findFromUser);
        }

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
                new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<Friend> findFriends = friendRepository.findByToUser(findUser);

        return findFriends.stream().map(FriendResponseDto::new).toList();
    }

    /**
     * 친구 수락 메서드. 본인의 친구창이 아닐 경우 예외 던짐
     *
     * @param userId    현재 친구 목록을 가진 유저 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     */
    @Transactional
    public void acceptFriend(Long friendId, Long userId, Long sessionId) {

        if (!userId.equals(sessionId)) {
            throw new CustomException(ErrorCode.INVALID_USER_NAME);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new CustomException(ErrorCode.FRIEND_NOT_FOUND)
        );

        findFriend.changeIsAccepted(true);

        // 서로 친구 생성
        Friend relatedFriend = new Friend(true, findFriend.getFromUser(), findFriend.getToUser());
        friendRepository.save(relatedFriend);
    }

    /**
     * 친구 삭제
     *
     * @param friendId  삭제할 친구 식별자
     * @param userId    현재 친구 목록을 가진 유저 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     */
    public void deleteFriend(Long friendId, Long userId, Long sessionId) {

        if (!userId.equals(sessionId)) {
            throw new CustomException(ErrorCode.INVALID_USER_NAME);
        }

        Friend findFriend = friendRepository.findById(friendId).orElseThrow(
                () -> new CustomException(ErrorCode.FRIEND_NOT_FOUND)
        );

        Friend findReversedFriend = friendRepository.findByToUserAndFromUser(findFriend.getFromUser(), findFriend.getToUser())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        friendRepository.delete(findFriend);
        friendRepository.delete(findReversedFriend);
    }
}
