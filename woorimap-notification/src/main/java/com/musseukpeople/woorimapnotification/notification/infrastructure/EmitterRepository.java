package com.musseukpeople.woorimapnotification.notification.infrastructure;

import java.util.Map;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    SseEmitter save(String id, SseEmitter sseEmitter);

    void saveEventCache(String id, Object data);

    Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId);

    Map<String, SseEmitter> findAllStartWithByMemberId(String memberId);

    void deleteById(String id);

    void deleteAllStartWithByMemberId(String memberId);

    void deleteAllEventCacheStartWithByMemberId(String memberId);
}
