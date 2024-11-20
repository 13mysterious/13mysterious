package com.example.newsfeed.service;

import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.service
 * <li>fileName       : CommentService
 * <li>date           : 24. 11. 20.
 * <li>description    :
 * </ul>
 */

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto createComment(Long boardId, Long userId, String contents) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        User findUser = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Comment comment = new Comment(contents, findBoard, findUser);
        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getUser().getName(), contents);
    }

    public List<CommentResponseDto> findAllCommentsByPage(Long boardId, Pageable pageable) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        List<Comment> allComments = commentRepository.findByBoardOrderByModifiedAtDesc(findBoard, pageable);

        return allComments.stream().map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, String contents) {

        Comment findComment = commentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Comment updatedComment = findComment.updateComment(contents);
        return new CommentResponseDto(updatedComment);
    }
}
