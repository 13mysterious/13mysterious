package com.example.newsfeed.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e){
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode(), e.getDetailMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleValidationException(MethodArgumentNotValidException e){
        String detailMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE, detailMessage);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseEntity> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_INPUT_VALUE, "잘못된 입력값입니다.");
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseEntity> handleGeneralException(Exception e){
        return ErrorResponseEntity.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");
    }

}
