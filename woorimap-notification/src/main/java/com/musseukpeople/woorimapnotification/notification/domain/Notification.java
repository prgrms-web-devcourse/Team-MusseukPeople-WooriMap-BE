package com.musseukpeople.woorimapnotification.notification.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "send_member_nick_name")
    private String senderNickName;

    @Column(nullable = false, name = "receive_member_id")
    private Long receiverId;

    @Column(nullable = false)
    private Long contentId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isRead;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDateTime;

    @Builder
    public Notification(String senderNickName, Long receiverId, Long contentId, NotificationType type, String content) {
        this.senderNickName = senderNickName;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.notificationType = type;
        this.content = content;
    }

    public void read() {
        this.isRead = true;
    }

    public enum NotificationType {
        POST_CREATED,
        POST_MODIFIED
    }
}
