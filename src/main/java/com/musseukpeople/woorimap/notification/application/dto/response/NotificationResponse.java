package com.musseukpeople.woorimap.notification.application.dto.response;

import com.musseukpeople.woorimap.notification.domain.Notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {

    private Long id;
    private Long contentId;
    private String nickName;
    private String content;

    public NotificationResponse(Long id, Long contentId, String nickName, String content) {
        this.id = id;
        this.contentId = contentId;
        this.nickName = nickName;
        this.content = content;
    }

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
            notification.getId(),
            notification.getContentId(),
            // TODO : API를 통해 NickName 반환받도록 변경
            String.valueOf(notification.getSenderId()),
            notification.getContent()
        );
    }
}
