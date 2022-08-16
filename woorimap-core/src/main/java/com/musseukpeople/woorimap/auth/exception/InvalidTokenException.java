package com.musseukpeople.woorimap.auth.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "유효하지 않은 토큰입니다. 토큰 : {0}";

    public InvalidTokenException(String token, ErrorCode errorCode) {
        super(format(ERROR_MESSAGE_FORMAT, token), errorCode);
    }
}
