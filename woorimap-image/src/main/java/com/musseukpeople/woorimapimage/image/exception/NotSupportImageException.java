package com.musseukpeople.woorimapimage.image.exception;

import java.text.MessageFormat;

import com.musseukpeople.woorimapimage.common.exception.ErrorCode;

public class NotSupportImageException extends FileException {
    private static final String ERROR_MESSAGE_FORMAT = "지원하지 않는 이미지 확장자입니다. 입력된 이미지의 확장자 : {0}";

    public NotSupportImageException(String extension, ErrorCode errorCode) {
        super(MessageFormat.format(ERROR_MESSAGE_FORMAT, extension), errorCode);
    }
}
