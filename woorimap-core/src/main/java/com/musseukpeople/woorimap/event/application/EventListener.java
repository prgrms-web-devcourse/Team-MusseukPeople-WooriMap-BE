package com.musseukpeople.woorimap.event.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.musseukpeople.woorimap.event.domain.LogoutMemberEvent;
import com.musseukpeople.woorimap.event.domain.PostEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventListener {

    private final Publisher publisher;

    @Async
    @TransactionalEventListener
    public void publishPostEvent(PostEvent postEvent) {
        publisher.publishPost(postEvent);
    }

    @Async
    @TransactionalEventListener
    public void publishLogoutEvent(LogoutMemberEvent event) {
        publisher.publishLogout(event.getMemberId());
    }
}
