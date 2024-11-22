package com.example.newsfeed.controller;
import com.example.newsfeed.config.Const;
import com.example.newsfeed.dto.*;
import com.example.newsfeed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시물 저장
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @RequestBody BoardCreateRequestDto requestDto,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId) {

        //게시물 저장
        BoardResponseDto boardResponseDto =
                boardService.createBoard(
                        requestDto.getTitle(),
                        requestDto.getContents(),
                        sessionId
                );

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }

    //게시물 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> findAllBoards(
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "1970-01-01") String startDate,
            @RequestParam(required = false, defaultValue = "2099-12-31") String endDate
    ) {
        List<BoardResponseDto> allBoardsDto = boardService.findAllBoards(sort,page, size,startDate,endDate);

        return new ResponseEntity<>(allBoardsDto, HttpStatus.OK);
    }

    //친구 게시물 조회
    @GetMapping("/friends")
    public ResponseEntity<List<BoardResponseDto>> findAllFriendsBoards(
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<BoardResponseDto> allFriendsBoards = boardService.findAllFriendsBoards(sessionId, page, size);

        return new ResponseEntity<>(allFriendsBoards, HttpStatus.OK);
    }

    /**
     * 좋아요 API
     * @param boardId 게시글 식별자
     * @param sessionId 로그인 식별자
     */
    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> createLikes(
            @PathVariable Long boardId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {
        boardService.createLikes(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 좋아요 취소 API
     * @param boardId 게시글 섹별자
     * @param sessionId 로그인 식별자
     * @return
     */
    @DeleteMapping("/{boardId}/likes")
    public ResponseEntity<Void> deleteLikes(
            @PathVariable Long boardId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {
        boardService.deleteLikes(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시물 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardUpdateResponseDto> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequestDto dto,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {

        BoardUpdateResponseDto boardUpdateResponseDto = boardService.updateBoard(boardId, dto.getTitle(), dto.getContents(), sessionId);
        return new ResponseEntity<>(boardUpdateResponseDto, HttpStatus.OK);
    }


    /**
     * 게시글 단건 조회
     *
     * @param boardId 게시글 식별자
     * @return 게시글 내용과 댓글 내용
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardFindOneWithCommentResponseDto> findBoardById(@PathVariable Long boardId) {

        BoardFindOneWithCommentResponseDto findBoard = boardService.findBoardById(boardId);

        return new ResponseEntity<>(findBoard, HttpStatus.OK);
    }


    /**
     * 게시글 삭제
     *
     * @param boardId     게시글 식별자
     * @param sessionId 로그인 식별자
     * @return 200
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @SessionAttribute(name = Const.SESSION_KEY) Long sessionId
    ) {
        boardService.deleteBoard(boardId, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
