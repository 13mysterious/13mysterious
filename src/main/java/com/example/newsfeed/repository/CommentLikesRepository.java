package com.example.newsfeed.repository;


import com.example.newsfeed.entity.CommentLikes;
import com.example.newsfeed.entity.CommentLikesPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, CommentLikesPK> {
}
