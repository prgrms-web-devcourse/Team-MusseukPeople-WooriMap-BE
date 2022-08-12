package com.musseukpeople.woorimap.notification.infrastructure;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.notification.domain.EmitterRepository;

@Repository
public class MemoryEmitterRepository implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    @Override
    public Optional<SseEmitter> findById(String emitterId) {
        if (!emitters.containsKey(emitterId)) {
            return Optional.empty();
        }
        return Optional.of(emitters.get(emitterId));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }
}
