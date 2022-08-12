package com.musseukpeople.woorimap.notification.application;

import static java.lang.String.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.notification.domain.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String CONNECT_EVENT_NAME = "connect check";
    private static final String CONNECT_EVENT_MESSAGE_FORMAT = "EventStream Created. [userId= %d]";

    private final EmitterRepository emitterRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendNotification(sseEmitter, CONNECT_EVENT_NAME, emitterId, format(CONNECT_EVENT_MESSAGE_FORMAT, memberId));
        return sseEmitter;
    }

    private void sendNotification(SseEmitter sseEmitter, String eventName, String emitterId, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(emitterId)
                .name(eventName)
                .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.info("sse 연결이 되지 않았습니다. emitterId : {}", emitterId);
        }
    }
}
