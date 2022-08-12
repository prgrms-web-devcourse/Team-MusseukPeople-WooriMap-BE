package com.musseukpeople.woorimap.notification.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.event.domain.PostEvent;
import com.musseukpeople.woorimap.event.domain.PostEvent.EventType;
import com.musseukpeople.woorimap.notification.infrastructure.EmitterRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @SpyBean
    private EmitterRepository emitterRepository;

    @DisplayName("알림 구독 성공")
    @Test
    void subscribe_success() {
        // given
        Long id = 1L;
        String lastEventId = null;

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
        PostEvent postEvent = new PostEvent(1L, receiverId, 1L, EventType.POST_CREATED, "test", LocalDateTime.now());
        given(emitterRepository.findAllStartWithByMemberId(anyString())).willReturn(
            Map.of(String.valueOf(receiverId), mockSseEmitter));

        // when
        notificationService.sendPostNotification(postEvent);

        // then
        then(mockSseEmitter).should(times(1)).send(any());
    }
}
