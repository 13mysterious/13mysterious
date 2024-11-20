package com.example.newsfeed.service;

import com.example.newsfeed.dto.BoardCreateResponseDto;
import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public BoardCreateResponseDto save(String title, String contents, @SessionAttribute(name ="userId") Long userId) {

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Board board = new Board(findUser, title, contents, 0);

        //게시물 저장
        boardRepository.save(board);

        return new BoardCreateResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContents()
        );
    }
    //게시물 목록 조회
    public List<BoardResponseDto> findAllBoards(Long userId, int page, int size) {

         Pageable pageable = PageRequest.of(page - 1, size);
         Page<Board> boardPage = boardRepository.findAllByUserId(userId, pageable);

         return boardPage.stream()
                 .map(board -> new BoardResponseDto(
                         board.getId(),
                         board.getTitle(),
                         board.getContents(),
                         board.getUser().getName(), // User 객체에서 이름을 가져옵니다.
                         board.getLikeCount(),
                         board.getCreatedAt(),
                         board.getModifiedAt()
                 ))
                 .collect(Collectors.toList());
    }
}
