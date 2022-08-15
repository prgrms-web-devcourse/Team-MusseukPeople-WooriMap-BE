package com.musseukpeople.woorimap.notification.application.message;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimap.event.domain.PostEvent;
import com.musseukpeople.woorimap.notification.application.NotificationService;

class RedisMessageSubscriberTest {

    private RedisMessageSubscriber redisMessageSubscriber;
    private ObjectMapper objectMapper;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        notificationService = mock(NotificationService.class);
        redisMessageSubscriber = new RedisMessageSubscriber(objectMapper, notificationService);
    }

    @DisplayName("이벤트 수신 성공")
    @Test
    void onMessage_success() throws JsonProcessingException {
        // given
        PostEvent postEvent = new PostEvent("test", 2L, 1L, PostEvent.EventType.POST_CREATED, "test", now());
        Message message = new DefaultMessage("2L".getBytes(), objectMapper.writeValueAsBytes(postEvent));

        // when
        // then
        assertAll(
            () -> assertDoesNotThrow(() -> redisMessageSubscriber.onMessage(message, "test".getBytes())),
            () -> then(notificationService).should(times(1)).sendPostNotification(any())
        );
    }

    @DisplayName("이벤트 타입이 다름으로 인한 실패")
    @Test
    void onMessage_invalidMessageType_fail() {
        // given
        String event = "invalidMessageType";
        Message message = new DefaultMessage("2L".getBytes(), event.getBytes());

        // when
        // then
        assertThatThrownBy(() -> redisMessageSubscriber.onMessage(message, "test".getBytes()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메시지를 변환하지 못했습니다.");
    }
}
