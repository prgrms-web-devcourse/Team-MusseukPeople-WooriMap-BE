package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class CreateCoupleFailException extends CoupleException {

    public CreateCoupleFailException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
