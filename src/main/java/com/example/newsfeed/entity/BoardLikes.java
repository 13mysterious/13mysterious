package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "likes")
public class BoardLikes {

    @EmbeddedId
    private BoardLikePK boardLikePK;

    public BoardLikes() {

    }

    public BoardLikes(User user, Board board){
        this.boardLikePK = new BoardLikePK(user,board);
    }

}
