package com.example.newsfeed.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 잘못된 입력값 */
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "잘못된 입력값입니다."),
    INVALID_INPUT_PARAM(HttpStatus.BAD_REQUEST, "INVALID_INPUT_PARAM", "잘못된 파라미터 값입니다."),
    INVALID_LIKE_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_LIKE_REQUEST", "본인이 작성자인 글에 좋아요를 누를 수 없습니다."),

    /* 401 로그인하지 않고 CRUD 접근 확인 */
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED,"INVALID_LOGIN","로그인 후에 사용해주세요."),
    /* 401 로그인, 작성자 이름 확인 */
    INVALID_USER_NAME(HttpStatus.UNAUTHORIZED,"INVALID_USER_NAME","로그인한 사용자와 작성자가 일치하지 않습니다."),
    /* 401 이메일 확인 */
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED,"INVALID_EMAIL","이메일이 일치하지 않습니다."),
    /* 401 비밀 번호 확인 */
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"INVALID_PASSWORD","비밀 번호가 일치하지 않습니다."),

    /* 404 찾을 수 없음*/
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND","해당 게시글을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND","해당 유저를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_NOT_FOUND","해당 댓글을 찾을 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE_NOT_FOUND", "해당 좋아요 기록을 찾을 수 없습니다."),

    /* 500 내부 서버 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "내부 서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
