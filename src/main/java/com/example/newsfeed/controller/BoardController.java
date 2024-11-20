package com.example.newsfeed.controller;


import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.dto.CreateBoardRequestDto;
import com.example.newsfeed.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> save(
            @RequestBody CreateBoardRequestDto requestDto,
            @SessionAttribute(name ="userId") Long userId) {

        //게시물 저장
        BoardResponseDto boardResponseDto =
                boardService.save(
                        requestDto.getTitle(),
                        requestDto.getContents(),
                        userId
                );

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }
}
