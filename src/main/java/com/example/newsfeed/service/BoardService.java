package com.example.newsfeed.service;

import com.example.newsfeed.dto.*;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Likes;
import com.example.newsfeed.entity.Comment;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.LikesRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final CommentRepository commentRepository;

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
    @Transactional
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

        Optional<Likes> likes = likesRepository.findByUserIdAndBoardId(sessionId, boardId);
        if (!likes.isEmpty() && likes.get().isLiked()) { // likes 있고, 좋아요 상태
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요 상태입니다.");
        } else if (!likes.isEmpty() && !likes.get().isLiked()) { // likes 있는데, 좋아요 취소 -> 토글
            sendLikesToggles(boardId, sessionId);
        } else if (likes.isEmpty()) {
            Likes currentBoard = new Likes(findLoginUser, findBoard);
            boolean status = !currentBoard.isLiked();
            currentBoard.setLiked(status);
            likesRepository.save(currentBoard);
            // 게시글 조회하여서 like count + 1 갱신
            findBoard.plusLikeCount();
            boardRepository.save(findBoard);
        }
    }

    /**
     * 좋아요 토글 메서드
     *
     * @param boardId   게시글 식별자
     * @param sessionId 로그인 식별자
     */
    public void sendLikesToggles(Long boardId, Long sessionId) {
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 게시글 작성자 조회
        Long findAuthorUserId = findBoard.getUser().getId();

        // 현재 어떤 유저가 로그인 했는지 조회
        User findLoginUser = userRepository.findById(sessionId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Optional<Likes> existLike = likesRepository.findByUserIdAndBoardId(sessionId, boardId);

        Likes likes;
        // 좋아요 엔티티 존재
        if (existLike.isPresent()) {
            likes = existLike.get();
            boolean status = !likes.isLiked();
            likes.setLiked(status);
            likesRepository.save(likes);
        } else {
            //자기 자신에게 좋아요 취소 할 수 없음
            if (sessionId == findAuthorUserId) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            likes = new Likes(findLoginUser, findBoard);
            boolean status = !likes.isLiked();
            likes.setLiked(status);
            likesRepository.save(likes);
        }

        if(likes.isLiked() == false){
            // 게시글 조회하여서 like count - 1 갱신
            findBoard.minusLikeCount();
            boardRepository.save(findBoard);
        }else if(likes.isLiked() == true){
            // 게시글 조회하여서 like count + 1 갱신
            findBoard.plusLikeCount();
            boardRepository.save(findBoard);
        }
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(Long boardId, String title, String contents, Long loginUserId) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (!loginUserId.equals(findBoard.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        findBoard.update(title, contents);
        return new BoardUpdateResponseDto(findBoard);
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

    /**
     * 게시글 삭제 메서드
     *
     * @param boardId     게시글 식별자
     * @param loginUserId 로그인 식별자
     */
    @Transactional
    public void deleteBoard(Long boardId, Long loginUserId) {
        // 삭제할 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 본인 글이 아닌 게시글을 삭제하려고 하는 경우
        if (loginUserId != findBoard.getUser().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        boardRepository.deleteById(boardId);

    }
}