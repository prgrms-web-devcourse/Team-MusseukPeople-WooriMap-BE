package com.musseukpeople.woorimap.member.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class MemberException extends BusinessException {

    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
