package com.musseukpeople.woorimapimage.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String message;
    private String code;

    private ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getMessage(), code.getCode());
    }

    public static ErrorResponse of(ErrorCode code, String message) {
        return new ErrorResponse(message, code.getCode());
    }
}
