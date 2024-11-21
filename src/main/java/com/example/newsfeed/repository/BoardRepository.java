package com.example.newsfeed.repository;

import com.example.newsfeed.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUserId(Long userId, Pageable pageable);

}
