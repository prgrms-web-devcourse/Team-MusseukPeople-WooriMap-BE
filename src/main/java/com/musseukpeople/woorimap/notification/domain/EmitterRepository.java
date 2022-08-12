package com.musseukpeople.woorimap.notification.domain;

import java.util.Map;
import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Optional<SseEmitter> findById(String id);

    void deleteById(String id);

    Map<String, SseEmitter> findAllStartWithById(String id);
}
