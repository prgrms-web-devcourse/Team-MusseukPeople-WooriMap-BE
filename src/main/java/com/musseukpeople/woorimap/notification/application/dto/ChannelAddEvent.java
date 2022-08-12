package com.musseukpeople.woorimap.notification.application.dto;

import lombok.Getter;

@Getter
public class ChannelAddEvent {

    private Long id;

    public ChannelAddEvent(Long id) {
        this.id = id;
    }
}
