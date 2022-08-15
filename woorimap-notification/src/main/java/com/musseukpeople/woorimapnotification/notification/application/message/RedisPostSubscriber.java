package com.musseukpeople.woorimapnotification.notification.application.message;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimapnotification.notification.application.NotificationFacade;
import com.musseukpeople.woorimapnotification.notification.domain.event.PostEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisPostSubscriber implements RedisSubscriber {

    private static final ChannelTopic POST_CHANNEL = ChannelTopic.of("post");
    private static final String CONVERT_FAIL_MESSAGE = "메시지를 변환하지 못했습니다.";

    private final ObjectMapper objectMapper;
    private final NotificationFacade notificationFacade;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        try {
            PostEvent postEvent = objectMapper.readValue(message.getBody(), PostEvent.class);
            notificationFacade.sendPostNotification(postEvent);
        } catch (IOException e) {
            throw new IllegalArgumentException(CONVERT_FAIL_MESSAGE, e);
        }
    }

    @Override
    public Topic getTopic() {
        return POST_CHANNEL;
    }
}
