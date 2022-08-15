package com.musseukpeople.woorimap.notification.application.dto.response;

import static com.musseukpeople.woorimap.notification.domain.Notification.*;

import com.musseukpeople.woorimap.notification.domain.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationResponse {

    @Schema(description = "알림 번호")
    private Long id;

    @Schema(description = "컨텐츠 번호")
    private Long contentId;

    @Schema(description = "알림 타입")
    private NotificationType type;

    @Schema(description = "알림 보낸 회원 닉네임")
    private String nickName;

    @Schema(description = "컨텐츠")
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
