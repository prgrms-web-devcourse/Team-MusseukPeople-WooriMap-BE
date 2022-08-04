package com.musseukpeople.woorimap.common.validator;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class InvalidImageUrlExcpetion extends BusinessException {

    private static final String ERROR_MESSAGE_FORMAT = "올바른 이미지 URL이 아닙니다. URL : {0}";

    public InvalidImageUrlExcpetion(String url, ErrorCode errorCode) {
        super(format(ERROR_MESSAGE_FORMAT, url), errorCode);
    }
}
