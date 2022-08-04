package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotCoupleException extends CoupleException {
    
    public NotCoupleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
