package com.musseukpeople.woorimap.event.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.musseukpeople.woorimap.event.domain.PostEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventListener {

    private final Publisher publisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(PostEvent postEvent) {
        publisher.publish(postEvent);
    }
}
