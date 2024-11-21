package com.example.newsfeed.service;

import com.example.newsfeed.dto.BoardFindResponseDto;
import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.dto.CommentWithDateResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public BoardResponseDto save(String title, String contents, @SessionAttribute(name ="userId") Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        Board board = new Board(findUser, title, contents, 0);

        //게시물 저장
        boardRepository.save(board);

        return new BoardResponseDto(board.getId(),board.getTitle(), board.getContents(), board.getUser().getName(), board.getLikeCount(), board.getCreatedAt(),board.getModifiedAt());
    }

    public BoardFindResponseDto findBoardById(Long boardId) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾지 못했습니다."));

        List<Comment> allComments = commentRepository.findByBoard(findBoard);

        List<CommentWithDateResponseDto> comments = allComments.stream().map(CommentWithDateResponseDto::new).toList();

        return new BoardFindResponseDto(
                findBoard.getId(),
                findBoard.getTitle(),
                findBoard.getContents(),
                findBoard.getUser().getName(),
                findBoard.getLikeCount(),
                comments,
                findBoard.getCreatedAt(),
                findBoard.getModifiedAt()
                );
    }
}
