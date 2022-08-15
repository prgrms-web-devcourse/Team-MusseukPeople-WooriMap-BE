package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class CoupleException extends BusinessException {

    public CoupleException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CoupleException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
