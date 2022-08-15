package com.musseukpeople.woorimapnotification.notification.application;

import static com.musseukpeople.woorimapnotification.notification.domain.Notification.*;
import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimapnotification.common.exception.ErrorCode;
import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.domain.NotificationRepository;
import com.musseukpeople.woorimapnotification.notification.domain.event.PostEvent;
import com.musseukpeople.woorimapnotification.notification.exception.NotFoundNotificationException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification create(PostEvent postEvent) {
        Notification notification = createNotification(postEvent);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void read(Long id) {
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

    private Notification createNotification(PostEvent postEvent) {
        return Notification.builder()
            .senderNickName(postEvent.getSourceNickName())
            .receiverId(postEvent.getDestinationId())
            .contentId(postEvent.getPostId())
            .content(postEvent.getContent())
            .type(NotificationType.valueOf(postEvent.getEventType().toString()))
            .build();
    }
}
