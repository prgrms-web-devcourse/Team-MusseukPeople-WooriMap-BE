package com.musseukpeople.woorimap.couple.exception;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class MappingCoupleMemberException extends CoupleException {

    public MappingCoupleMemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
