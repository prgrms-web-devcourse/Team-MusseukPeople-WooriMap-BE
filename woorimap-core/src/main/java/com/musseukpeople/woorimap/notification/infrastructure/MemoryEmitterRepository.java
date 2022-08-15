package com.musseukpeople.woorimap.notification.infrastructure;

import static java.util.Map.*;
import static java.util.stream.Collectors.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class MemoryEmitterRepository implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String id, SseEmitter sseEmitter) {
        emitters.put(id, sseEmitter);
        return sseEmitter;
    }

    @Override
    public void saveEventCache(String id, Object data) {
        eventCache.put(id, data);
    }

    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(memberId))
            .collect(toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public Map<String, SseEmitter> findAllStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(memberId))
            .collect(toMap(Entry::getKey, Entry::getValue));
    }

    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    @Override
    public void deleteAllStartWithByMemberId(String memberId) {
        for (String key : emitters.keySet()) {
            if (key.startsWith(memberId)) {
                emitters.remove(key);
            }
        }
    }

    @Override
    public void deleteAllEventCacheStartWithByMemberId(String memberId) {
        for (String key : eventCache.keySet()) {
            if (key.startsWith(memberId)) {
                eventCache.remove(key);
            }
        }
    }
}
