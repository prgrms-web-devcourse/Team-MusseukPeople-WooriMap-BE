package com.musseukpeople.woorimapimage.common.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", " 서버 에러입니다."),

    INVALID_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "I001", "지원하지 않는 이미지 확장자입니다."),
    EXCEED_IMAGE_SIZE(HttpStatus.BAD_REQUEST, "I002", "이미지 용량이 너무 큽니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
