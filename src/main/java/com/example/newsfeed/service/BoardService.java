package com.example.newsfeed.service;

import com.example.newsfeed.dto.BoardCreateResponseDto;
import com.example.newsfeed.dto.BoardResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Likes;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.LikesRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    public BoardCreateResponseDto save(String title, String contents, @SessionAttribute(name = "userId") Long userId) {

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


    /**
     * 좋아요 누르는 메서드
     *
     * @param boardId   게시글 식별자
     * @param sessionId 로그인 식별자
     */
    public void sendLikes(Long boardId, Long sessionId) {
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 현재 어떤 유저가 로그인 했는지 조회
        User findLoginUser = userRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // 게시글 작성자 조회
        Long findAuthorUserId = findBoard.getUser().getId();
        //자기 자신에게 좋아요 할 수 없음
        if (sessionId == findAuthorUserId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Optional<Likes> likes = likesRepository.findLikesByUserId(sessionId);
        if (!likes.isEmpty() && likes.get().isLiked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요 상태입니다.");
        }
        Likes currentBoard = new Likes(findLoginUser, findBoard);
        currentBoard.changeIsLiked();
        likesRepository.save(currentBoard);

    }

    /**
     * 좋아요 취소 메서드
     *
     * @param boardId   게시글 식별자
     * @param sessionId 로그인 식별자
     */
    public void sendUnLikes(Long boardId, Long sessionId) {
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 현재 어떤 유저가 로그인 했는지 조회
        User findLoginUser = userRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // 게시글 작성자 조회
        Long findAutorUserId = findBoard.getUser().getId();

        //자기 자신에게 좋아요 취소 할 수 없음
        if (sessionId == findAutorUserId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // likes 테이블에서 현재 로그인한 사람이 해당 게시글을 로그인했는지 조회
        Optional<Likes> likes = Optional.ofNullable(likesRepository.findLikesByUserId(sessionId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)));
        if (!likes.get().isLiked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요 취소 상태입니다.");

        }
        likes.get().changeIsLiked();
        likesRepository.save(likes.get());
    }

    /**
     * 좋아요 토글 메서드
     *
     * @param boardId   게시글 식별자
     * @param sessionId 로그인 식별자
     */
    public void sendLikesToggles(Long boardId, Long sessionId) {
        Optional<Likes> existLike = likesRepository.findByUserIdAndBoardId(sessionId, boardId);

        if (existLike.isPresent()) {
            Likes likes = existLike.get();
            boolean status = !likes.isLiked();
            likes.setLiked(status);
            likesRepository.save(likes);
        } else {
            // 게시글 식별자로 게시글 조회
            Board findBoard = boardRepository.findById(boardId)
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

            // 현재 어떤 유저가 로그인 했는지 조회
            User findLoginUser = userRepository.findById(sessionId)
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.UNAUTHORIZED));

            // 게시글 작성자 조회
            Long findAuthorUserId = findBoard.getUser().getId();

            Likes currentBoard = new Likes(findLoginUser, findBoard);
            currentBoard.changeIsLiked();
            likesRepository.save(currentBoard);
        }
    }

}
