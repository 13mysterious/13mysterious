package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Friend;
import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByToUser(User user);

    List<Friend> findByFromUser_Id(Long UserId);

    List<Friend> findByFromUser_IdAndToUser_Id(Long fromUserId, Long toUserId);


}
