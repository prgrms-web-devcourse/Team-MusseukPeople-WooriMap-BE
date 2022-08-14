package com.musseukpeople.woorimapnotification.notification.application;

import static com.musseukpeople.woorimapnotification.notification.domain.Notification.NotificationType.*;
import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;
import com.musseukpeople.woorimapnotification.notification.domain.Notification;
import com.musseukpeople.woorimapnotification.notification.domain.NotificationRepository;
import com.musseukpeople.woorimapnotification.notification.domain.PostEvent;
import com.musseukpeople.woorimapnotification.notification.domain.PostEvent.EventType;
import com.musseukpeople.woorimapnotification.notification.exception.NotFoundNotificationException;
import com.musseukpeople.woorimapnotification.notification.infrastructure.EmitterRepository;
import com.musseukpeople.woorimapnotification.notification.util.NotificationTest;

class NotificationServiceTest extends NotificationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @SpyBean
    private EmitterRepository emitterRepository;

    @DisplayName("알림 구독 성공")
    @Test
    void subscribe_success() {
        // given
        Long id = 1L;
        String lastEventId = "";

        // when
        SseEmitter sseEmitter = notificationService.subscribe(id, lastEventId);

        // then
        assertAll(
            () -> assertThat(sseEmitter).isNotNull(),
            () -> then(emitterRepository).should(times(1)).save(anyString(), any())
        );
    }

    @DisplayName("알림 메시지 전송 성공")
    @Test
    void sendPostNotification() throws IOException {
        // given
        Long receiverId = 2L;
        SseEmitter mockSseEmitter = mock(SseEmitter.class);
        PostEvent postEvent = new PostEvent("test", receiverId, 1L, EventType.POST_CREATED, "test", now());
        given(emitterRepository.findAllStartWithByMemberId(anyString())).willReturn(
            Map.of(String.valueOf(receiverId), mockSseEmitter));

        // when
        notificationService.sendPostNotification(postEvent);

        // then
        then(mockSseEmitter).should(times(1)).send(any());
    }

    @DisplayName("알림 읽음 처리 성공")
    @Test
    void readNotification_success() {
        // given
        Long notificationId = notificationRepository.save(createNotification(1L))
            .getId();

        // when
        notificationService.readNotification(notificationId);

        // then
        Notification notification = notificationRepository.findById(notificationId).get();
        assertThat(notification.isRead()).isTrue();

    }

    @DisplayName("존재하지 않는 알림으로 인한 읽음 처리 실패")
    @Test
    void readNotification_notFound_fail() {
        // given
        Long notificationId = 1L;

        // when
        // then
        assertThatThrownBy(() -> notificationService.readNotification(notificationId))
            .isInstanceOf(NotFoundNotificationException.class)
            .hasMessageContaining("존재하지 않는 알림입니다. ");
    }

    @DisplayName("읽지 않은 알림 조회 성공")
    @Test
    void getUnreadNotifications_success() {
        // given
        Long memberId = 1L;
        Notification notification = createNotification(1L);
        notification.read();
        Notification otherNotification = createNotification(1L);
        notificationRepository.saveAll(List.of(notification, otherNotification));

        // when
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(memberId);

        // then
        assertThat(notifications).hasSize(1);
    }

    public Notification createNotification(Long memberId) {
        return Notification.builder()
            .receiverId(memberId)
            .senderNickName("우리맵")
            .contentId(1L)
            .type(POST_CREATED)
            .content("test")
            .build();
    }
}
