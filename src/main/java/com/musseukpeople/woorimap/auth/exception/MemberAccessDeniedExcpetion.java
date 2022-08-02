package com.musseukpeople.woorimap.auth.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class MemberAccessDeniedExcpetion extends BusinessException {
    
    public MemberAccessDeniedExcpetion(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
