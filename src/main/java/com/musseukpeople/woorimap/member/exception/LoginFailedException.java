package com.musseukpeople.woorimap.member.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class LoginFailedException extends MemberException {

    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
