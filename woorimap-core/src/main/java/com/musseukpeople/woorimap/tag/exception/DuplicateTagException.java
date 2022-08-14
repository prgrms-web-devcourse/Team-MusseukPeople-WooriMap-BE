package com.musseukpeople.woorimap.tag.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class DuplicateTagException extends BusinessException {
    public DuplicateTagException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }
}
