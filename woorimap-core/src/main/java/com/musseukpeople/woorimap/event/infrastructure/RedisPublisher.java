package com.musseukpeople.woorimap.event.infrastructure;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.event.application.Publisher;
import com.musseukpeople.woorimap.event.domain.PostEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisPublisher implements Publisher {

    private static final String POST_CHANNEL_NAME = "post";
    private static final String LOGOUT_CHANNEL_NAME = "logout";

    private final RedisTemplate<String, PostEvent> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisPublisher(RedisTemplate<String, PostEvent> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(PostEvent.class));
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void publishPost(PostEvent postEvent) {
        redisTemplate.convertAndSend(POST_CHANNEL_NAME, postEvent);
    }

    @Override
    public void publishLogout(String memberId) {
        stringRedisTemplate.convertAndSend(LOGOUT_CHANNEL_NAME, memberId);
    }
}
