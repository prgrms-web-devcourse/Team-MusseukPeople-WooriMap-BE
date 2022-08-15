package com.musseukpeople.woorimap.couple.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundCoupleException extends CoupleException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 커플입니다. 커플 번호: {0}";

    public NotFoundCoupleException(ErrorCode errorCode, Long coupleId) {
        super(format(ERROR_MESSAGE_FORMAT, coupleId), errorCode);
    }
}
