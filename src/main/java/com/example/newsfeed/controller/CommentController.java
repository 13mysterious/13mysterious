package com.example.newsfeed.controller;

import com.example.newsfeed.dto.CommentRequestDto;
import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     *
     * @param boardId 댓글을 작성할 게시글 식별자
     * @param dto     댓글 contents 포함하는 요청 dto
     * @param userId  댓글 작성하는 유저 식별자
     * @return 작성된 댓글 정보
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long boardId,
            @RequestBody CommentRequestDto dto,
            @SessionAttribute(name = "userId") Long userId
    ) {

        CommentResponseDto commentResponseDto = commentService.createComment(boardId, userId, dto.getContents());
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    /**
     * 게시글 내의 전체 댓글을 페이징하여 조회
     *
     * @param boardId 게시글 식별자
     * @param page    페이지 번호
     * @param size    페이지 크기
     * @return 페이징된 댓글 목록
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllCommentsByPage(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        List<CommentResponseDto> allComments = commentService.findAllCommentsByPage(boardId, pageable);
        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    /**
     * 댓글 내용 수정
     *
     * @param boardId   게시글 식별자
     * @param commentId 댓글 식별자
     * @param dto       수정할 contents 포함한 요청 dto
     * @return 수정된 댓글
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto
    ) {

        CommentResponseDto commentResponseDto = commentService.updateComment(commentId, dto.getContents());
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    /**
     * 댓글 삭제. 댓글 작성자 또는 게시글 작성자만 삭제 가능.
     *
     * @param boardId     게시글 식별자
     * @param commentId   댓글 식별자
     * @param loginUserId 현재 로그인한 유저 식별자
     * @return 상태코드
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute(name = "userId") Long loginUserId
    ) {

        commentService.deleteComment(commentId, boardId, loginUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
