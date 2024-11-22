package com.example.newsfeed.service;

import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.entity.*;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.CommentLikesRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentLikesRepository commentLikesRepository;

    /**
     * 댓글 작성
     *
     * @param boardId  게시글 식별자
     * @param sessionId   댓글 작성자 식별자
     * @param contents 내용
     * @return 작성된 댓글과 id dto
     */
    public CommentResponseDto createComment(Long boardId, Long sessionId, String contents) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );
        User findUser = userRepository.findById(sessionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Comment comment = new Comment(contents, findBoard, findUser);
        Comment createdComment = commentRepository.save(comment);

        return new CommentResponseDto(createdComment);
    }

    /**
     * 게시글별 페이징된 댓글 조회
     *
     * @param boardId  게시글 식별자
     * @param pageable 페이징 객체
     * @return 페이징된 댓글 목록
     */
    public List<CommentResponseDto> findAllCommentsByPage(Long boardId, Pageable pageable) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        List<Comment> allComments = commentRepository.findByBoardOrderByModifiedAtDesc(findBoard, pageable);

        return allComments.stream().map(CommentResponseDto::new).toList();
    }

    /**
     * 댓글 수정
     *
     * @param id       댓글 식별자
     * @param contents 수정할 내용
     * @return 수정된 댓글
     */
    @Transactional
    public CommentResponseDto updateComment(Long id, String contents) {

        Comment findComment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        findComment.updateComment(contents);
        return new CommentResponseDto(findComment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId   댓글 식별자
     * @param boardId     게시글 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     */
    public void deleteComment(Long commentId, Long boardId, Long sessionId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );
        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );

        // 로그인한 사람이 댓글 작성자도, 게시글 작성자도 아닐 때
        if (!sessionId.equals(findComment.getUser().getId()) && !sessionId.equals(findBoard.getUser().getId())) {
            throw new CustomException(ErrorCode.INVALID_USER_NAME);
        }

        commentRepository.delete(findComment);
    }

    /**
     * 댓글 좋아요 생성
     *
     * @param commentId   댓글 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     * @return 1, 좋아요 수 1 증가
     */
    @Transactional
    public int createLike(Long commentId, Long sessionId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        User findUser = userRepository.findById(sessionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        // 이미 좋아요가 눌려있다면 예외 발생
        boolean isLikePresent = commentLikesRepository.findById(new CommentLikesPK(findComment, findUser)).isPresent();
        if (findComment.getUser().equals(findUser) || isLikePresent) {
            throw new CustomException(ErrorCode.INVALID_LIKE_REQUEST);
        }

        CommentLikes like = new CommentLikes(new CommentLikesPK(findComment, findUser));
        commentLikesRepository.save(like);

        return 1;
    }

    /**
     * 좋아요 취소(삭제)
     *
     * @param commentId   댓글 식별자
     * @param sessionId 현재 로그인한 유저 식별자
     * @return -1, 좋아요 수 1 차감
     */
    @Transactional
    public int deleteLike(Long commentId, Long sessionId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(
                () -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        User findUser = userRepository.findById(sessionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        CommentLikes findLike = commentLikesRepository.findById(new CommentLikesPK(findComment, findUser)).orElseThrow(
                () -> new CustomException(ErrorCode.LIKE_NOT_FOUND)
        );

        commentLikesRepository.delete(findLike);

        return -1;
    }

    /**
     * 좋아요 개수 갱신
     *
     * @param commentId        댓글 식별자
     * @param likeCountChanged 좋아요 개수 변경값
     */
    @Transactional
    public void updateLikeCount(Long commentId, int likeCountChanged) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorCode.COMMENT_NOT_FOUND)
        );

        findComment.updateLikeCount(likeCountChanged);
        commentRepository.save(findComment);
    }
}
