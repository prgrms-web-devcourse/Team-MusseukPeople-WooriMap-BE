package com.musseukpeople.woorimap.notification.application;

import static com.musseukpeople.woorimap.notification.domain.Notification.*;
import static java.lang.String.*;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.event.domain.PostEvent;
import com.musseukpeople.woorimap.notification.application.dto.ChannelAddEvent;
import com.musseukpeople.woorimap.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimap.notification.domain.EmitterRepository;
import com.musseukpeople.woorimap.notification.domain.Notification;
import com.musseukpeople.woorimap.notification.domain.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String CONNECT_EVENT_NAME = "connect check";
    private static final String CONNECT_EVENT_MESSAGE_FORMAT = "EventStream Created. [userId= %d]";
    private static final String ID_DELIMITER = "_";

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    // TODO : lastEventId로 유실된 이벤트 전송해야 함
    @Transactional
    public SseEmitter subscribe(Long memberId, String lastEventId) {
        String emitterId = createEmitterId(memberId);
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendConnectNotification(sseEmitter, emitterId, memberId);
        applicationEventPublisher.publishEvent(new ChannelAddEvent(memberId));
        return sseEmitter;
    }

    @Transactional
    public void sendPostNotification(PostEvent postEvent) {
        Notification notification = notificationRepository.save(createNotification(postEvent));
        String id = notification.getReceiverId() + ID_DELIMITER;
        String eventId = createEmitterId(notification.getReceiverId());
        Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithById(id);
        emitters.forEach(
            (key, emitter) -> sendNotification(emitter, eventId, notification.getEventName(), key,
                NotificationResponse.from(notification))
        );
    }

    private void sendConnectNotification(SseEmitter sseEmitter, String emitterId, Long memberId) {
        sendNotification(sseEmitter, emitterId, CONNECT_EVENT_NAME, emitterId,
            format(CONNECT_EVENT_MESSAGE_FORMAT, memberId));
    }

    private void sendNotification(SseEmitter sseEmitter, String eventId, String eventName, String emitterId,
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

    private Notification createNotification(PostEvent postEvent) {
        return Notification.builder()
            .senderId(postEvent.getSourceId())
            .receiverId(postEvent.getDestinationId())
            .contentId(postEvent.getPostId())
            .content(postEvent.getContent())
            .type(NotificationType.valueOf(postEvent.getEventType().toString()))
            .build();
    }

    private String createEmitterId(Long memberId) {
        return memberId + ID_DELIMITER + System.currentTimeMillis();
    }
}
