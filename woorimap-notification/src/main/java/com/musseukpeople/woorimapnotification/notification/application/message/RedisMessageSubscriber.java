package com.musseukpeople.woorimapnotification.notification.application.message;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimapnotification.notification.application.NotificationService;
import com.musseukpeople.woorimapnotification.notification.domain.PostEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private static final String CONVERT_FAIL_MESSAGE = "메시지를 변환하지 못했습니다.";

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostEvent postEvent = objectMapper.readValue(message.getBody(), PostEvent.class);
            notificationService.sendPostNotification(postEvent);
        } catch (IOException e) {
            throw new IllegalArgumentException(CONVERT_FAIL_MESSAGE, e);
        }
    }
}
