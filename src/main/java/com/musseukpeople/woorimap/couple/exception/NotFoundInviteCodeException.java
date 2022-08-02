package com.musseukpeople.woorimap.couple.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundInviteCodeException extends CoupleException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 코드입니다. 커플 코드: {0}";

    public NotFoundInviteCodeException(ErrorCode errorCode, String inviteCode) {
        super(format(ERROR_MESSAGE_FORMAT, inviteCode), errorCode);
    }
}
