package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<Comment>();

    public Board() {

    }

    public Board(User user, String title, String contents, int likeCount) {
        this.user = user;
        this.title = title;
        this.contents = contents;
        this.likeCount = likeCount;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void addLikeCount(){
        this.likeCount = this.likeCount + 1;
    }

    public void removeLikeCount(){
        this.likeCount = this.likeCount - 1;
    }

}
