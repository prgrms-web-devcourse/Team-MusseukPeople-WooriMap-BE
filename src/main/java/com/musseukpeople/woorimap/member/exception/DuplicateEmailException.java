package com.musseukpeople.woorimap.member.exception;

import java.text.MessageFormat;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class DuplicateEmailException extends MemberException {

    private static final String ERROR_MESSAGE_FORMAT = "중복된 이메일입니다. 현재 이메일 : {0}";

    public DuplicateEmailException(String value, ErrorCode errorCode) {
        super(MessageFormat.format(ERROR_MESSAGE_FORMAT, value), errorCode);
    }
}
