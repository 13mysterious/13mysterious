package com.example.newsfeed.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Comment() {

    }

    public Comment(String contents, Board board, User user) {
        this.contents = contents;
        this.board = board;
        this.user = user;
    }

    /**
     * 댓글 내용 수정
     *
     * @param contents 수정할 내용
     */
    public void updateComment(String contents) {
        this.contents = contents;
    }

    public void updateLikeCount(int count) {
        this.likeCount += count;
    }
}
