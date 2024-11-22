package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUserId(Long userId, Pageable pageable);
    Page<Board> findAllByUserIdIn(List<Long> userIds, Pageable pageable);

    Page<Board> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
