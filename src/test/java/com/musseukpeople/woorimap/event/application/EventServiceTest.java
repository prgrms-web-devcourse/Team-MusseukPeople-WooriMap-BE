package com.musseukpeople.woorimap.event.application;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.musseukpeople.woorimap.event.domain.PostEvent;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private Publisher publisher;

    @DisplayName("이벤트 발행 성공")
    @Test
    void publishEvent_success() {
        // given
        PostEvent postEvent = new PostEvent(1L, 2L, 1L, "타이틀", LocalDateTime.now());

        // when
        eventService.publishEvent(postEvent);

        // then
        then(publisher).should(times(1)).publish(any());
    }
}
