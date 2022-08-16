package com.musseukpeople.woorimapimage.image.exception;

import java.text.MessageFormat;

import com.musseukpeople.woorimapimage.common.exception.ErrorCode;

public class ConvertFileFailedException extends FileException {
    private static final String ERROR_MESSAGE_FORMAT = "MultiPartFile 변환에 실패했습니다. 파일명 : {0}";

    public ConvertFileFailedException(String fileName, ErrorCode errorCode) {
        super(MessageFormat.format(ERROR_MESSAGE_FORMAT, fileName), errorCode);
    }
}
