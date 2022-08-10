package com.musseukpeople.woorimap.member.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundMemberException extends MemberException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 회원입니다. 회원 번호: {0}";

    public NotFoundMemberException(ErrorCode errorCode, Long id) {
        super(format(ERROR_MESSAGE_FORMAT, id), errorCode);
    }
}
