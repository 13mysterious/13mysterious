package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.repository
 * <li>fileName       : FriendRepository
 * <li>date           : 24. 11. 20.
 * <li>description    : friend 테이블 레포지토리
 * </ul>
 */

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Long> findByFromUser_Id(Long UserId);


    default List<Friend> findALlById(Long id) {
        return findAll().stream()
                .filter(friend -> friend.getId().equals(id))
                .toList();
    }
}
