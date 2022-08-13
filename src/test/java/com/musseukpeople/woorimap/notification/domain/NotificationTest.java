package com.musseukpeople.woorimap.notification.domain;

import static com.musseukpeople.woorimap.notification.domain.Notification.NotificationType.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.musseukpeople.woorimap.notification.domain.Notification.NotificationType;

class NotificationTest {

    @DisplayName("알림 객체 생성 성공")
    @Test
    void create_success() {
        // given
        Long senderId = 1L;
        Long receiverId = 2L;
        Long contentId = 1L;
        NotificationType notificationType = POST_CREATED;
        String content = "test";

        // when
        Notification notification = new Notification(senderId, receiverId, contentId, notificationType, content);

        // then
        assertThat(notification).isNotNull();
    }

    @DisplayName("알림 읽음 성공")
    @Test
    void read_success() {
        // given
        Notification notification = new Notification(1L, 2L, 1L, POST_CREATED, "test");

        // when
        notification.read();

        // then
        assertThat(notification.isRead()).isTrue();
    }
}
