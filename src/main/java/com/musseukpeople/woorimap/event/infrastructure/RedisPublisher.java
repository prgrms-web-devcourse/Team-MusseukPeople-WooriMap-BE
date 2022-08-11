package com.musseukpeople.woorimap.event.infrastructure;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.event.application.Publisher;
import com.musseukpeople.woorimap.event.domain.PostEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher implements Publisher {

    public static final String CHANNEL_PREFIX = "channel/";

    private final RedisTemplate<String, PostEvent> redisTemplate;

    @Override
    public void publish(PostEvent postEvent) {
        String channel = String.valueOf(postEvent.getDestinationId());
        redisTemplate.convertAndSend(CHANNEL_PREFIX + channel, postEvent);
    }
}
