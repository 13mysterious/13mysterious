package com.example.newsfeed.service;

import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.dto.BoardUpdateResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardResponseDto save(String title, String contents, @SessionAttribute(name ="userId") Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        Board board = new Board(findUser, title, contents, 0);

        //게시물 저장
        boardRepository.save(board);

        return new BoardResponseDto(board.getId(),board.getTitle(), board.getContents());
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(Long boardId, String title, String contents, Long loginUserId) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if(!loginUserId.equals(findBoard.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        findBoard.update(title, contents);
        return new BoardUpdateResponseDto(findBoard);
    }
}
