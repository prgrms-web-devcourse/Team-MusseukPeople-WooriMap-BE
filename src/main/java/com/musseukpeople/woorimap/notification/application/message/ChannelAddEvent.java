package com.musseukpeople.woorimap.notification.application.message;

import lombok.Getter;

@Getter
public class ChannelAddEvent {

    private Long id;

    public ChannelAddEvent(Long id) {
        this.id = id;
    }
}
