package com.musseukpeople.woorimap.notification.domain;

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

    @Column(nullable = false, name = "send_member_id")
    private Long senderId;

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
    public Notification(Long senderId, Long receiverId, Long contentId, NotificationType type, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.contentId = contentId;
        this.notificationType = type;
        this.content = content;
    }

    public void read() {
        this.isRead = true;
    }

    public String getEventName() {
        return this.notificationType.getName();
    }

    public enum NotificationType {
        POST_CREATED("created"),
        POST_MODIFIED("modified");

        private final String name;

        NotificationType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
