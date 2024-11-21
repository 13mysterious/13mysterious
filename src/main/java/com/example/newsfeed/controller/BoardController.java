package com.example.newsfeed.controller;
import com.example.newsfeed.dto.*;
import com.example.newsfeed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시물 저장
    @PostMapping
    public ResponseEntity<BoardCreateResponseDto> save(
            @RequestBody CreateBoardRequestDto requestDto,
            @SessionAttribute(name = "userId") Long userId) {

        //게시물 저장
        BoardCreateResponseDto boardCreateResponseDto =
                boardService.save(
                        requestDto.getTitle(),
                        requestDto.getContents(),
                        userId
                );

        return new ResponseEntity<>(boardCreateResponseDto, HttpStatus.CREATED);
    }

    //게시물 목록 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<BoardResponseDto>> findAllBoards(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<BoardResponseDto> allBoardsDto = boardService.findAllBoards(userId, page, size);

        return new ResponseEntity<>(allBoardsDto, HttpStatus.OK);
    }

    //친구 게시물 조회
    @GetMapping("/friends")
    public ResponseEntity<List<BoardResponseDto>> findAllFriendsBoards(
            @RequestParam Long fromUserId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<BoardResponseDto> allFriendsBoards = boardService.findAllFriendsBoards(fromUserId, page, size);

        return new ResponseEntity<>(allFriendsBoards, HttpStatus.OK);
    }
  
    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> sendLikes(
            @PathVariable Long boardId,
            @SessionAttribute(name = "userId") Long sessionId
    ) {
        boardService.sendLikes(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{boardId}/likes")
    public ResponseEntity<Void> sendLikesToggles(
            @PathVariable Long boardId,
            @SessionAttribute(name = "userId") Long sessionId
    ) {
        boardService.sendLikesToggles(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 게시물 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardUpdateResponseDto> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateRequestDto dto,
            @SessionAttribute(name = "userId") Long loginUserId
    ) {

        BoardUpdateResponseDto boardUpdateResponseDto = boardService.updateBoard(boardId, dto.getTitle(), dto.getContents(), loginUserId);
        return new ResponseEntity<>(boardUpdateResponseDto, HttpStatus.OK);
    }


    //게시글 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardFindResponseDto> findBoardById(@PathVariable Long boardId) {

        BoardFindResponseDto findBoard = boardService.findBoardById(boardId);

        return new ResponseEntity<>(findBoard, HttpStatus.OK);
    }


    /**
     * 게시글 삭제
     *
     * @param boardId     게시글 식별자
     * @param loginUserId 로그인 식별자
     * @return
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @SessionAttribute(name = "userId") Long loginUserId
    ) {
        boardService.deleteBoard(boardId, loginUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}