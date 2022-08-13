package com.musseukpeople.woorimap.notification.exception;

import com.musseukpeople.woorimap.common.exception.BusinessException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

public class NotificationException extends BusinessException {
    
    public NotificationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
