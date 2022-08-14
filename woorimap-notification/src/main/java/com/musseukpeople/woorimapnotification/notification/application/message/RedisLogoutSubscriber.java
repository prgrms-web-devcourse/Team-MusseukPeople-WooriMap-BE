package com.musseukpeople.woorimapnotification.notification.application.message;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimapnotification.notification.application.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLogoutSubscriber implements RedisSubscriber {

    private static final ChannelTopic LOGOUT_CHANNEL = ChannelTopic.of("logout");

    private final NotificationService notificationService;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        notificationService.deleteAllEmitterByMemberId(message.toString());
    }

    @Override
    public Topic getTopic() {
        return LOGOUT_CHANNEL;
    }
}
