package com.example.newsfeed.controller;

import com.example.newsfeed.dto.CommentRequestDto;
import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <ul>
 * <li>packageName    : com.example.newsfeed.controller
 * <li>fileName       : CommentController
 * <li>date           : 24. 11. 20.
 * <li>description    : 댓글 기능 관리 컨트롤러
 * </ul>
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto dto,
            @SessionAttribute(name = "userId") Long userId
    ) {

        CommentResponseDto commentResponseDto = commentService.createComment(boardId, userId, dto.getContents());
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllCommentsByPage(
            @PathVariable Long boardId,
            @RequestParam int page,
            @RequestParam int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        List<CommentResponseDto> allComments = commentService.findAllCommentsByPage(boardId, pageable);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto
    ) {

        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, dto.getContents());
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }
}
