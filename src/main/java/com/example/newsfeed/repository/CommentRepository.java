package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.repository
 * <li>fileName       : CommentRepository
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardOrderByModifiedAtDesc(Board board, Pageable pageable);
}
