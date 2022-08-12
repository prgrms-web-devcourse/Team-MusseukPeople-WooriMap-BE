package com.musseukpeople.woorimap.notification.application.message;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisMessageListener {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisMessageSubscriber redisMessageSubscriber;

    @TransactionalEventListener
    public void addRedisMessageListener(ChannelAddEvent event) {
        ChannelTopic channel = ChannelTopic.of("post/" + event.getId());
        redisMessageListenerContainer.addMessageListener(redisMessageSubscriber, channel);
    }
}
