package com.musseukpeople.woorimap.member.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundMemberException extends MemberException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 회원 이메일입니다. 이메일 : {0}";

    public NotFoundMemberException(ErrorCode errorCode, String email) {
        super(format(ERROR_MESSAGE_FORMAT, email), errorCode);
    }
}
