package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Friend;
import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByToUser(User user);

    List<Friend> findByFromUser_IdAndIsAcceptedTrue(Long userId);

    Optional<Friend> findByToUserAndFromUser(User toUser, User fromUser);
}
