package com.musseukpeople.woorimapnotification.notification.infrastructure;

import static com.musseukpeople.woorimapnotification.notification.domain.Notification.NotificationType.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimapnotification.notification.application.dto.response.NotificationResponse;

class EmitterRepositoryTest {

    private EmitterRepository emitterRepository;

    @BeforeEach
    void setUp() {
        emitterRepository = new MemoryEmitterRepository();
    }

    @DisplayName("Emitter 저장 성공")
    @Test
    void save_success() {
        // given
        String emitterId = "1";
        SseEmitter sseEmitter = new SseEmitter();

        // when
        emitterRepository.save(emitterId, sseEmitter);

        // then
        assertThat(emitterRepository.findAllStartWithByMemberId(emitterId)).hasSize(1);
    }

    @DisplayName("수신한 이벤트 저장 성공")
    @Test
    void saveEventCache_success() {
        // given
        String id = "1";
        NotificationResponse data = new NotificationResponse(1L, 1L, POST_CREATED, "test", "content");

        // when
        emitterRepository.saveEventCache(id, data);

        // then
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(id);
        assertThat(events).hasSize(1);
    }

    @DisplayName("Emitter 삭제 성공")
    @Test
    void deleteById_success() {
        // given
        String emitterId = "1L";
        SseEmitter sseEmitter = new SseEmitter();
        emitterRepository.save(emitterId, sseEmitter);

        // when
        emitterRepository.deleteById(emitterId);

        // then
        assertThat(emitterRepository.findAllStartWithByMemberId(emitterId)).isEmpty();
    }

    @DisplayName("회원이 접속한 모든 Emitter 조회 성공")
    @Test
    void findAllStartWithById_success() {
        // given
        String memberId = "1";
        String emitterId = memberId + "_" + "1";
        emitterRepository.save(emitterId, new SseEmitter());

        String otherEmitterId = memberId + "_" + "2";
        emitterRepository.save(otherEmitterId, new SseEmitter());

        // when
        Map<String, SseEmitter> result = emitterRepository.findAllStartWithByMemberId(memberId);

        // then
        assertThat(result).hasSize(2);
    }

    @DisplayName("회원에게 수신된 이벤트 조회 성공")
    @Test
    void findAllEventCacheStartWithById_success() {
        // given
        String memberId = "1";
        String emitterId = memberId + "_" + "1";
        emitterRepository.saveEventCache(emitterId, "test");

        String otherEmitterId = memberId + "_" + "2";
        emitterRepository.saveEventCache(otherEmitterId, "test");

        // when
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(memberId);

        // then
        assertThat(events).hasSize(2);
    }

    @DisplayName("회원의 모든 SseEmitter 삭제 성공")
    @Test
    void deleteAllStartWithByMemberId_success() {
        // given
        String memberId = "1";
        String emitterId = memberId + "_" + "1";
        emitterRepository.save(emitterId, new SseEmitter());

        String otherEmitterId = memberId + "_" + "2";
        emitterRepository.save(otherEmitterId, new SseEmitter());

        // when
        emitterRepository.deleteAllStartWithByMemberId(memberId);

        // then
        assertThat(emitterRepository.findAllStartWithByMemberId(memberId)).isEmpty();
    }

    @DisplayName("회원이 수신한 모든 이벤트 삭제 성공")
    @Test
    void deleteAllEventCacheStartWithByMemberId_success() {
        // given
        String memberId = "1";
        String emitterId = memberId + "_" + "1";
        emitterRepository.saveEventCache(emitterId, "test");

        String otherEmitterId = memberId + "_" + "2";
        emitterRepository.saveEventCache(otherEmitterId, "test");

        // when
        emitterRepository.deleteAllEventCacheStartWithByMemberId(memberId);

        // then
        assertThat(emitterRepository.findAllEventCacheStartWithByMemberId(memberId)).isEmpty();
    }
}
