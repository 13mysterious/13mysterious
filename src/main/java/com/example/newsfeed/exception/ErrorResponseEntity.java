package com.example.newsfeed.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;
    private String detailMessage;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e,String detailMessage){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .code(e.name())
                        .message(e.getMessage())
                        .detailMessage(detailMessage)
                        .build()
                );
    }
}
