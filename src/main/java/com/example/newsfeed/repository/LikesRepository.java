package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findLikesByUserId(Long userId);
    Optional<Likes> findByUserIdAndBoardId(Long sessionId, Long boardId);
}
