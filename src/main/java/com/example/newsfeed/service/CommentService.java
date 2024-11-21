package com.example.newsfeed.service;

import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.repository.BoardRepository;
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

    /**
     * 댓글 작성
     *
     * @param boardId  게시글 식별자
     * @param userId   댓글 작성자 식별자
     * @param contents 내용
     * @return 작성된 댓글과 id dto
     */
    public CommentResponseDto createComment(Long boardId, Long userId, String contents) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        User findUser = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        Comment comment = new Comment(contents, findBoard, findUser);
        commentRepository.save(comment);

        return new CommentResponseDto(comment.getId(), comment.getUser().getName(), contents);
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
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
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        findComment.updateComment(contents);
        return new CommentResponseDto(findComment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId   댓글 식별자
     * @param boardId     게시글 식별자
     * @param loginUserId 현재 로그인한 유저 식별자
     */
    public void deleteComment(Long commentId, Long boardId, Long loginUserId) {

        Comment findComment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        // 로그인한 사람이 댓글 작성자도, 게시글 작성자도 아닐 때
        if (!loginUserId.equals(findComment.getUser().getId()) && !loginUserId.equals(findBoard.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        commentRepository.delete(findComment);
    }
}
