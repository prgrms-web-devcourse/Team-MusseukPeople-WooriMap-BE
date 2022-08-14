package com.musseukpeople.woorimapnotification.notification.exception;

import com.musseukpeople.woorimapnotification.common.exception.ErrorCode;

import lombok.Getter;

@Getter
public class NotificationException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotificationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
