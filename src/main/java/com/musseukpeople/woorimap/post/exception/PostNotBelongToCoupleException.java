package com.musseukpeople.woorimap.post.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class PostNotBelongToCoupleException extends PostException {

    private static final String MESSAGE_FORMAT = "해당하는 사용자의 게시물이 아닙니다. 커플 : {0}, 게시물 : {1}";

    public PostNotBelongToCoupleException(Long coupleId, Long postId, ErrorCode errorCode) {
        super(format(MESSAGE_FORMAT, coupleId, postId), errorCode);
    }
}
