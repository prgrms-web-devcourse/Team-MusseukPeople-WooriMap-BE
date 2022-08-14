package com.musseukpeople.woorimap.auth.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class InvalidTokenRequestException extends BusinessException {
    public InvalidTokenRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
