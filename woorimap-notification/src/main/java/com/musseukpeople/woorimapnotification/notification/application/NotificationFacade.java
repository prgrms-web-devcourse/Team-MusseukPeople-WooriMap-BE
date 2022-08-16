package com.musseukpeople.woorimapnotification.notification.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.domain.event.PostEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationFacade {

    private final EmitterService emitterService;
    private final NotificationService notificationService;

    @Transactional
    public SseEmitter subscribe(Long memberId, String lastEventId) {
        return emitterService.create(memberId, lastEventId);
    }

    @Transactional
    public void sendPostNotification(PostEvent postEvent) {
        Notification notification = notificationService.create(postEvent);
        emitterService.sendNotification(notification);
    }

    @Transactional
    public void deleteAllMemberEmitter(String memberId) {
        emitterService.deleteAllMemberEmitter(memberId);
    }

    @Transactional
    public void readNotification(Long id) {
        notificationService.read(id);
    }

    public List<NotificationResponse> getUnreadNotifications(Long memberId) {
        return notificationService.getUnreadNotifications(memberId);
    }
}
