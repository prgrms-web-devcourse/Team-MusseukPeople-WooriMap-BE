package com.musseukpeople.woorimap.notification.application.message;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimap.event.domain.PostEvent;
import com.musseukpeople.woorimap.notification.application.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostEvent postEvent = objectMapper.readValue(message.getBody(), PostEvent.class);
            notificationService.sendPostNotification(postEvent);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("메시지를 변환하지 못했습니다.");
        }
    }
}
