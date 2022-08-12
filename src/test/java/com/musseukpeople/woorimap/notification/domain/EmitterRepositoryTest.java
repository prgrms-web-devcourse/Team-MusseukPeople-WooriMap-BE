package com.musseukpeople.woorimap.notification.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.musseukpeople.woorimap.notification.infrastructure.MemoryEmitterRepository;

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
        String emitterId = "1L";
        SseEmitter sseEmitter = new SseEmitter();

        // when
        emitterRepository.save(emitterId, sseEmitter);

        // then
        SseEmitter findEmitter = emitterRepository.findById(emitterId).get();
        assertThat(findEmitter).isEqualTo(sseEmitter);
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
        assertThat(emitterRepository.findById(emitterId)).isEmpty();
    }

    @DisplayName("회원이 접속한 모든 Emitter 조회 성공")
    @Test
    void findAllStartWithById_success() {
        // given
        String memberId = "1";
        String emitterId = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(emitterId, new SseEmitter());

        String otherEmitterId = memberId + "_" + System.currentTimeMillis();
        emitterRepository.save(otherEmitterId, new SseEmitter());

        // when
        Map<String, SseEmitter> result = emitterRepository.findAllStartWithById(memberId);

        // then
        assertThat(result).hasSize(2);
    }
}
