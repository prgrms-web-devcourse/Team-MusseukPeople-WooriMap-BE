package com.musseukpeople.woorimap.post.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class PostException extends BusinessException {

    public PostException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
