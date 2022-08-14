package com.musseukpeople.woorimap.auth.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class MemberAccessDeniedException extends BusinessException {

    public MemberAccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
