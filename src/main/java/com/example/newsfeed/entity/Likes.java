package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "likes")
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isLiked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public Likes() {

    }

    public Likes(User user, Board board){
        this.user = user;
        this.board = board;
    }

    public void setLiked(boolean isLiked){
        this.isLiked = isLiked;
    }

    public void changeIsLiked(){
        this.isLiked = !this.isLiked;
    }
}
