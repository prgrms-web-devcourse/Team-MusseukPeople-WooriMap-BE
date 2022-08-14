package com.musseukpeople.woorimap.notification.application.dto.response;

import static com.musseukpeople.woorimap.notification.domain.Notification.*;

import com.musseukpeople.woorimap.notification.domain.Notification;

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
