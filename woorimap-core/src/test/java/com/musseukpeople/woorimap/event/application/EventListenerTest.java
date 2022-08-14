package com.musseukpeople.woorimap.event.application;

import static java.time.LocalDateTime.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.musseukpeople.woorimap.event.domain.LogoutMemberEvent;
import com.musseukpeople.woorimap.event.domain.PostEvent;

@ExtendWith(MockitoExtension.class)
class EventListenerTest {

    @InjectMocks
    private EventListener eventListener;

    @Mock
    private Publisher publisher;

    @DisplayName("게시글 이벤트 발행 성공")
    @Test
    void publishEvent_success() {
        // given
        PostEvent postEvent = new PostEvent("test", 2L, 1L, PostEvent.EventType.POST_CREATED, "타이틀", now());

        // when
        eventListener.publishPostEvent(postEvent);

        // then
        then(publisher).should(times(1)).publishPost(any());
    }

    @DisplayName("로그아웃 이벤트 발행 성공")
    @Test
    void publishLogoutEvent_success() {
        // given
        LogoutMemberEvent logoutMemberEvent = new LogoutMemberEvent("1");

        eventListener.publishLogoutEvent(logoutMemberEvent);

        // then
        then(publisher).should(times(1)).publishLogout(any());
    }
}
