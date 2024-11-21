package com.example.newsfeed.controller;


import com.example.newsfeed.dto.BoardCreateResponseDto;
import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.dto.CreateBoardRequestDto;
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
            @SessionAttribute(name ="userId") Long userId) {

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
            @RequestParam(defaultValue = "10") int size)
    {
        List<BoardResponseDto> allBoardsDto = boardService.findAllBoards(userId, page, size);

        return new ResponseEntity<>(allBoardsDto, HttpStatus.OK);
    }


    @PostMapping("/{boardId}/likes")
    public ResponseEntity<Void> sendLikes(
            @PathVariable Long boardId,
            @SessionAttribute(name = "userId") Long sessionId
    ){
        boardService.sendLikes(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{boardId}/likes")
    public ResponseEntity<Void> sendLikesToggles(
            @PathVariable Long boardId,
            @SessionAttribute(name = "userId") Long sessionId
    ){
        boardService.sendLikesToggles(boardId, sessionId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}