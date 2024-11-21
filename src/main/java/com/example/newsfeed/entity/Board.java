package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "board")
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false ,columnDefinition = "longtext")
    private String contents;

    @Column(nullable = false)
    private int likeCount;

    public Board() {

    }

    public Board(User user, String title, String contents, int likeCount) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.likeCount = likeCount;
    }
}
