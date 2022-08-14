package com.musseukpeople.woorimapnotification.notification.application;

import static java.lang.String.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimapnotification.common.exception.ErrorCode;
import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.domain.Notification.NotificationType;
import com.musseukpeople.woorimapnotification.notification.domain.NotificationRepository;
import com.musseukpeople.woorimapnotification.notification.domain.PostEvent;
import com.musseukpeople.woorimapnotification.notification.exception.NotFoundNotificationException;
import com.musseukpeople.woorimapnotification.notification.infrastructure.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// TODO : Facade로 변경
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String EVENT_NAME = "sse";
    private static final String CONNECT_EVENT_MESSAGE_FORMAT = "EventStream Created. [userId= %d]";
    private static final String ID_DELIMITER = "_";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public SseEmitter subscribe(Long memberId, String lastEventId) {
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
    public void sendPostNotification(PostEvent postEvent) {
        Notification notification = notificationRepository.save(createNotification(postEvent));
        String id = notification.getReceiverId() + ID_DELIMITER;
        String eventId = createEmitterId(notification.getReceiverId());
        Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithByMemberId(id);
        NotificationResponse notificationResponse = NotificationResponse.from(notification);
        emitters.forEach((key, emitter) -> {
                emitterRepository.saveEventCache(key, notificationResponse);
                sendNotification(emitter, eventId, key, notificationResponse);
            }
        );
    }

    @Transactional
    public void deleteAllEmitterByMemberId(String memberId) {
        emitterRepository.deleteAllStartWithByMemberId(memberId);
        emitterRepository.deleteAllEventCacheStartWithByMemberId(memberId);
    }

    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotFoundNotificationException(id, ErrorCode.NOT_FOUND_NOTIFICATION));

        notification.read();
    }

    public List<NotificationResponse> getUnreadNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findUnreadAllByMemberId(memberId);
        return notifications.stream()
            .map(NotificationResponse::from)
            .collect(toList());
    }

    private boolean hasLostEvent(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private String createEmitterId(Long memberId) {
        return memberId + ID_DELIMITER + System.currentTimeMillis();
    }

    private Notification createNotification(PostEvent postEvent) {
        return Notification.builder()
            .senderNickName(postEvent.getSourceNickName())
            .receiverId(postEvent.getDestinationId())
            .contentId(postEvent.getPostId())
            .content(postEvent.getContent())
            .type(NotificationType.valueOf(postEvent.getEventType().toString()))
            .build();
    }

    private void sendConnectNotification(SseEmitter sseEmitter, String emitterId, Long memberId) {
        sendNotification(sseEmitter, emitterId, emitterId, format(CONNECT_EVENT_MESSAGE_FORMAT, memberId));
    }

    private void sendLostEvent(Long memberId, String lastEventId, String emitterId, SseEmitter sseEmitter) {
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(memberId + ID_DELIMITER);
        events.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
            .forEach(entry -> sendNotification(sseEmitter, entry.getKey(), emitterId, entry.getValue()));
    }

    private void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(eventId)
                .name(EVENT_NAME)
                .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            log.info("이벤트 전송을 실패했습니다. emitterId : {}", emitterId);
        }
    }
}
