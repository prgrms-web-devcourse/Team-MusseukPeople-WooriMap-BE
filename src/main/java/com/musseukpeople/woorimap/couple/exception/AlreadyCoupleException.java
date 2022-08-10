package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class AlreadyCoupleException extends CoupleException {

    public AlreadyCoupleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
