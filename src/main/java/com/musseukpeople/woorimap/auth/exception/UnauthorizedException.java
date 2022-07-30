package com.musseukpeople.woorimap.auth.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
