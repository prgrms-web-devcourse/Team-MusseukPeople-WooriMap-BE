package com.musseukpeople.woorimapnotification.notification.application.dto.response;

import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.domain.Notification.NotificationType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {

    private Long id;
    private Long contentId;
    private NotificationType type;
    private String nickName;
    private String content;

    public NotificationResponse(Long id, Long contentId, NotificationType type, String nickName, String content) {
        this.id = id;
        this.contentId = contentId;
        this.type = type;
        this.nickName = nickName;
        this.content = content;
    }

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getContentId(),
            notification.getNotificationType(),
            notification.getSenderNickName(),
            notification.getContent()
        );
    }
}
