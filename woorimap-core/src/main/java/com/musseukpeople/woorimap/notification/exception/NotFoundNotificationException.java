package com.musseukpeople.woorimap.notification.exception;

import static java.text.MessageFormat.*;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotFoundNotificationException extends NotificationException {

    private static final String ERROR_MESSAGE_FORMAT = "존재하지 않는 알림입니다. 알림 번호: {0}";

    public NotFoundNotificationException(Long id, ErrorCode errorCode) {
        super(format(ERROR_MESSAGE_FORMAT, id), errorCode);
    }
}
