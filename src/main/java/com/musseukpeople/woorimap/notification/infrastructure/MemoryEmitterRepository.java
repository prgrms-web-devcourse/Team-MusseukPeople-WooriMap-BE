package com.musseukpeople.woorimap.notification.infrastructure;

import static java.util.Map.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public Optional<SseEmitter> findById(String id) {
        if (!emitters.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(emitters.get(id));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public Map<String, SseEmitter> findAllStartWithById(String id) {
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(id))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }
}
