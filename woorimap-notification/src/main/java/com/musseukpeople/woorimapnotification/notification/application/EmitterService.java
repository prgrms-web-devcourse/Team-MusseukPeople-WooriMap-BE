package com.musseukpeople.woorimapnotification.notification.application;

import static java.lang.String.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.infrastructure.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmitterService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String EVENT_NAME = "sse";
    private static final String CONNECT_EVENT_MESSAGE_FORMAT = "EventStream Created. [userId= %d]";
    private static final String ID_DELIMITER = "_";

    private final EmitterRepository emitterRepository;

    @Transactional
    public SseEmitter create(Long memberId, String lastEventId) {
        String emitterId = createEmitterId(memberId);
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendConnectNotification(sseEmitter, emitterId, memberId);

        if (hasLostEvent(lastEventId)) {
            sendLostEvent(memberId, lastEventId, emitterId, sseEmitter);
        }

        return sseEmitter;
    }

    @Transactional
    public void sendNotification(Notification notification) {
        String id = notification.getReceiverId() + ID_DELIMITER;
        String eventId = createEmitterId(notification.getReceiverId());
        Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithByMemberId(id);
        NotificationResponse notificationResponse = NotificationResponse.from(notification);
        emitters.forEach((key, emitter) -> {
                emitterRepository.saveEventCache(key, notificationResponse);
                sendNotification(emitter, EVENT_NAME, eventId, key, notificationResponse);
            }
        );
    }

    @Transactional
    public void deleteAllMemberEmitter(String memberId) {
        emitterRepository.deleteAllStartWithByMemberId(memberId + ID_DELIMITER);
        emitterRepository.deleteAllEventCacheStartWithByMemberId(memberId + ID_DELIMITER);
    }

    private boolean hasLostEvent(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private String createEmitterId(Long memberId) {
        return memberId + ID_DELIMITER + System.currentTimeMillis();
    }

    private void sendConnectNotification(SseEmitter sseEmitter, String emitterId, Long memberId) {
        sendNotification(sseEmitter, "connect", emitterId, emitterId, format(CONNECT_EVENT_MESSAGE_FORMAT, memberId));
    }

    private void sendLostEvent(Long memberId, String lastEventId, String emitterId, SseEmitter sseEmitter) {
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(memberId + ID_DELIMITER);
        events.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
            .forEach(entry -> sendNotification(sseEmitter, EVENT_NAME, entry.getKey(), emitterId, entry.getValue()));
    }

    private void sendNotification(SseEmitter sseEmitter, String eventName, String eventId, String emitterId,
                                  Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(eventId)
                .name(eventName)
                .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.info("이벤트 전송을 실패했습니다. emitterId : {}", emitterId);
        }
    }
}
