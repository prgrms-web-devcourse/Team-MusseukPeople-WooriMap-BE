package com.musseukpeople.woorimap.notification.domain;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String emitterId, Object data);

    Map<String, Object> findAllEventCacheStartWithById(String id);

    void deleteById(String id);

    Map<String, SseEmitter> findAllStartWithById(String id);
}
