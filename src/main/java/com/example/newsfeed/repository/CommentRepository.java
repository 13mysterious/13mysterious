package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * {@link Board} 기준으로 Comment를 찾아서 수정일 기준 내림차순 정렬, 페이징
     *
     * @param board    기준이 되는 게시글
     * @param pageable 페이징
     * @return 기준과 일치하고, 정렬 및 페이징된 Comment 목록
     */
    List<Comment> findByBoardOrderByModifiedAtDesc(Board board, Pageable pageable);

    /**
     * {@link Board} 기준으로 Comment를 찾음
     *
     * @param board 기준이 되는 게시글
     * @return 찾은 댓글
     */
    List<Comment> findByBoard(Board board);
}
