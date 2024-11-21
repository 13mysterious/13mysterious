package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.BoardLikePK;
import com.example.newsfeed.entity.BoardLikes;
import com.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<BoardLikes, BoardLikePK> {
    Optional<BoardLikes> findByBoardLikePK_UserAndBoardLikePK_Board(User findLoginUser, Board findBoard);
}
