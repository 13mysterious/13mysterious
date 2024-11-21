package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@Entity
@AllArgsConstructor
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean isAccepted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    public Friend() {

    }

    public Friend(boolean isAccepted, User toUser, User fromUser) {
        this.isAccepted = isAccepted;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    /**
     * 친구 수락
     *
     * @param status 요청 상태
     */
    public void changeIsAccepted(boolean status) {
        isAccepted = status;
    }

}
