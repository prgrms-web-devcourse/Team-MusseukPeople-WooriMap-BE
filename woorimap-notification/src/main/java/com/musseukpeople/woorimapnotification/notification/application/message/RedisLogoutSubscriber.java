package com.musseukpeople.woorimapnotification.notification.application.message;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimapnotification.notification.application.NotificationFacade;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisLogoutSubscriber implements RedisSubscriber {

    private static final ChannelTopic LOGOUT_CHANNEL = ChannelTopic.of("logout");

    private final NotificationFacade notificationFacade;

    @Override
    @Transactional
    public void onMessage(Message message, byte[] pattern) {
        notificationFacade.deleteAllMemberEmitter(message.toString());
    }

    @Override
    public Topic getTopic() {
        return LOGOUT_CHANNEL;
    }
}
