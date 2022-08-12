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

    public static final String POST_CHANNEL_NAME = "post";

    private final RedisTemplate<String, PostEvent> redisTemplate;

    public RedisPublisher(RedisTemplate<String, PostEvent> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PostEvent.class));
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void publish(PostEvent postEvent) {
        redisTemplate.convertAndSend(POST_CHANNEL_NAME, postEvent);
    }
}
