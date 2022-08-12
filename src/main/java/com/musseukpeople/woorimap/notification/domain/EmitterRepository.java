package com.musseukpeople.woorimap.notification.domain;

import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Optional<SseEmitter> findById(String emitterId);

    void deleteById(String emitterId);
}
