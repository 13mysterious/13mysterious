package com.example.newsfeed.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.entity
 * <li>fileName       : CommentLikesPK
 * <li>date           : 24. 11. 21.
 * <li>description    :
 * </ul>
 */

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikesPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
