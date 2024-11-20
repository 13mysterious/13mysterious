package com.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.entity
 * <li>fileName       : Friend
 * <li>date           : 24. 11. 20.
 * <li>description    : friend 테이블 엔티티
 * </ul>
 */

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

    public void changeIsAccepted(boolean status) {
        isAccepted = status;
    }

}
