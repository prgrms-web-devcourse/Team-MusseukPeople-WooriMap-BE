package com.musseukpeople.woorimap.notification.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.util.IntegrationTest;

class NotificationServiceTest extends IntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @DisplayName("알림 구독 성공")
    @Test
    void subscribe_success() {
        // given
        Long id = 1L;
        String lastEventId = null;

        // when
        SseEmitter sseEmitter = notificationService.subscribe(id, lastEventId);

        // then
        assertThat(sseEmitter).isNotNull();
    }
}
