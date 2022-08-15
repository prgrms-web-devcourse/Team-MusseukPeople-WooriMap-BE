package com.musseukpeople.woorimapimage.image.exception;

import com.musseukpeople.woorimapimage.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {

    private final ErrorCode errorCode;

    public FileException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
