package com.example.newsfeed.service;

import com.example.newsfeed.dto.*;
import com.example.newsfeed.entity.*;
import com.example.newsfeed.entity.Board;
import com.example.newsfeed.entity.Comment;
import com.example.newsfeed.entity.BoardLikes;
import com.example.newsfeed.entity.User;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.repository.BoardRepository;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.LikesRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.newsfeed.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final FriendRepository friendRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시물 작성 메서드
     * @param title
     * @param contents
     * @param sessionId
     * @return 작성한 게시글 출력
     */
    public BoardResponseDto createBoard(String title, String contents, Long sessionId) {

        User findUser = userRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Board board = new Board(findUser, title, contents, 0);

        //게시물 저장
        Board createdBoard = boardRepository.save(board);

        return new BoardResponseDto(
                createdBoard.getId(),
                createdBoard.getTitle(),
                createdBoard.getContents(),
                createdBoard.getUser().getName(),
                createdBoard.getLikeCount(),
                createdBoard.getCreatedAt(),
                createdBoard.getModifiedAt()
        );
    }

    /**
     * 게시글 목록 조회
     * @param sortType 정렬 타입
     * @param page 페이지 번호
     * @param size 페이지 사이즈
     * @param start 기간별 조회 시 시작 날짜
     * @param end 기간별 조회 시 마지막 날짜
     * @return 게시물 리스트 출력
     */
    public List<BoardResponseDto> findAllBoards(String sortType, int page, int size,String start,String end) {

        Pageable pageable;
        Page<Board> boardPage;
        if("likes".equals(sortType)){
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("likeCount")));
            boardPage = boardRepository.findAll(pageable);
        }else if("period".equals(sortType)){
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("modifiedAt")));
            boardPage = boardRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay(),pageable);
        }else if("modifiedAt".equals(sortType)){
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("modifiedAt")));
            boardPage = boardRepository.findAll(pageable);
        }
        else{
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
            boardPage = boardRepository.findAll(pageable);
        }


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
     * 게시글 단건 조회 메서드
     *
     * @param boardId 게시글 식별자
     * @return 게시글과 그 게시글에 달린 댓글
     */
    public BoardFindOneWithCommentResponseDto findBoardById(Long boardId) {

        // 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException(BOARD_NOT_FOUND));

        // 조회한 게시글에 달린 댓글 조회
        List<Comment> allComments = commentRepository.findByBoard(findBoard);

        // 조회한 댓글을 목록으로 저장
        List<CommentResponseDto> comments = allComments.stream().map(CommentResponseDto::new).toList();
        BoardResponseDto boardResponseDto = new BoardResponseDto( findBoard.getId(),
                findBoard.getTitle(),
                findBoard.getContents(),
                findBoard.getUser().getName(),
                findBoard.getLikeCount(),
                findBoard.getCreatedAt(),
                findBoard.getModifiedAt());

        return new BoardFindOneWithCommentResponseDto(
                boardResponseDto,
                comments
        );
    }

    /**
     * 좋아요 처리 메서드
     *
     * @param boardId   게시글 식별자
     * @param sessionId 로그인 식별자
     */
    @Transactional
    public void createLikes(Long boardId, Long sessionId) {
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new CustomException(BOARD_NOT_FOUND));

        // 게시글 작성자 조회
        Long findAuthorUserId = findBoard.getUser().getId();

        // 현재 어떤 유저가 로그인 했는지 조회
        User findLoginUser = userRepository.findById(sessionId)
                .orElseThrow(() ->
                        new CustomException(USER_NOT_FOUND));

        //자기 자신에게 좋아요 할 수 없음
        if (sessionId == findAuthorUserId) {
            throw new CustomException(INVALID_LIKE_REQUEST);
        }

        // BoardLikes 엔티티 조회
        Optional<BoardLikes> likes = likesRepository.findByBoardLikePK_UserAndBoardLikePK_Board(findLoginUser, findBoard);

        if (likes.isPresent() == true) { // boardLikes 있음, 좋아요 상태
            throw new CustomException(INVALID_LIKE_ALREADY);
        }

        // 좋아요 생성
        BoardLikes boardLikes = new BoardLikes(findLoginUser, findBoard);
        likesRepository.save(boardLikes);

        //좋아요 카운팅
        updateBoardLikeCount(boardId, true);
    }

    /**
     * 좋아요 취소 메서드
     * @param boardId 게시글 식별자
     * @param sessionId 로그인 식별자
     */
    @Transactional
    public void deleteLikes(Long boardId, Long sessionId){
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new CustomException(BOARD_NOT_FOUND));

        // 게시글 작성자 조회
        Long findAuthorUserId = findBoard.getUser().getId();

        // 현재 어떤 유저가 로그인 했는지 조회
        User findLoginUser = userRepository.findById(sessionId)
                .orElseThrow(() ->
                        new CustomException(USER_NOT_FOUND));

        //자기 자신에게 좋아요 할 수 없음
        if (sessionId == findAuthorUserId) {
            throw new CustomException(INVALID_UNLIKE_REQUEST);
        }

        // BoardLikes 엔티티 조회
        Optional<BoardLikes> likes = likesRepository.findByBoardLikePK_UserAndBoardLikePK_Board(findLoginUser, findBoard);

        if (likes.isPresent() == false) { // boardLikes 없음, 좋아요 취소 상태
            throw new CustomException(INVALID_UNLIKE_ALREADY);
        }

        // 좋아요 삭제
        BoardLikes boardLikes = new BoardLikes(findLoginUser, findBoard);
        likesRepository.delete(boardLikes);

        // 좋아요 카운팅
        updateBoardLikeCount(boardId, false);
    }

    /**
     * 게시물 수정
     * @param boardId 게시글 식별자
     * @param title 수정할 게시글 제목
     * @param contents 수정할 게시글 내용
     * @param sessionId 로그인한 유저
     * @return 게시글 업데이트 내용
     */
    @Transactional
    public BoardUpdateResponseDto updateBoard(Long boardId, String title, String contents, Long sessionId) {

        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException(BOARD_NOT_FOUND)
        );

        if (!sessionId.equals(findBoard.getUser().getId())) {
            throw new CustomException(INVALID_USER_NAME);
        }

        findBoard.update(title, contents);
        return new BoardUpdateResponseDto(findBoard);
    }



    /**
     * 게시글 삭제 메서드
     *
     * @param boardId     게시글 식별자
     * @param sessionId 로그인 식별자
     */
    @Transactional
    public void deleteBoard(Long boardId, Long sessionId) {
        // 삭제할 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new CustomException(BOARD_NOT_FOUND));

        // 본인 글이 아닌 게시글을 삭제하려고 하는 경우
        if (sessionId != findBoard.getUser().getId()) {
            throw new CustomException(INVALID_USER_NAME);
        }

        boardRepository.deleteById(boardId);

    }

    //친구 게시물 목록 조회
    public List<BoardResponseDto> findAllFriendsBoards(Long sessionId, int page, int size) {
        //주어진 현재 사용자의ID(ssessionId)를 통해 친구 목록을 가져옴
        List<Friend> friendIds = friendRepository.findByFromUser_IdAndIsAcceptedTrue(sessionId);

        //친구 ID 글 목록 추출
        List<Long> friendsIds = friendIds.stream()
                .map(friend -> friend.getToUser().getId())
                .collect(Collectors.toList());


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        //친구 게시물 목록 조회
        Page<Board> friendBoardPage = boardRepository.findAllByUserIdIn(friendsIds, pageable);

        return friendBoardPage.stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContents(),
                        board.getUser().getName(),
                        board.getLikeCount(),
                        board.getCreatedAt(),
                        board.getModifiedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 좋아요 개수 카운트 하는 메서드
     * @param boardId
     * @param flagLikes
     */
    @Transactional
    public void updateBoardLikeCount(Long boardId, boolean flagLikes){
        // 게시글 식별자로 게시글 조회
        Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new CustomException(BOARD_NOT_FOUND));

        if(flagLikes){
            // 게시글 조회하여서 like count + 1 갱신
            findBoard.addLikeCount();
        }else{
            // 게시글 조회하여서 like count - 1 갱신
            findBoard.removeLikeCount();
        }

        boardRepository.save(findBoard);
    }
}
