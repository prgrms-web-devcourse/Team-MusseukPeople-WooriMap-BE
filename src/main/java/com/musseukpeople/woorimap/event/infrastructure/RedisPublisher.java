package com.musseukpeople.woorimap.event.infrastructure;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.event.application.Publisher;
import com.musseukpeople.woorimap.event.domain.PostEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisPublisher implements Publisher {

    public static final String CHANNEL_PREFIX = "channel/";

    private final RedisTemplate<String, PostEvent> redisTemplate;

    public RedisPublisher(RedisTemplate<String, PostEvent> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PostEvent.class));
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(PostEvent postEvent) {
        String channel = String.valueOf(postEvent.getDestinationId());
        redisTemplate.convertAndSend(CHANNEL_PREFIX + channel, postEvent);
    }
}
