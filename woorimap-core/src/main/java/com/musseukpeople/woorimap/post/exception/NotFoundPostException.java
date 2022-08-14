package com.musseukpeople.woorimap.post.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundPostException extends PostException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 게시글입니다. 게시글 번호: {0}";

    public NotFoundPostException(ErrorCode errorCode, Long postId) {
        super(format(ERROR_MESSAGE_FORMAT, postId), errorCode);
    }
}
