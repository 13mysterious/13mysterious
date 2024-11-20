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
 * <li>description    : 댓글 레포지토리
 * </ul>
 */

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * {@code Board} 기준으로 Comment를 찾아서 수정일 기준 내림차순 정렬, 페이징
     *
     * @param board 기준이 되는 {@code Board}
     * @param pageable 페이징
     * @return 기준과 일치하고, 정렬 및 페이징된 Comment 목록
     */
    List<Comment> findByBoardOrderByModifiedAtDesc(Board board, Pageable pageable);
}
