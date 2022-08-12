package com.musseukpeople.woorimap.notification.domain;

import static org.assertj.core.api.Assertions.*;

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
}
