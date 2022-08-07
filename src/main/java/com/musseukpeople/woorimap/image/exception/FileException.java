package com.musseukpeople.woorimap.image.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class FileException extends BusinessException {

    public FileException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
