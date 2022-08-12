package com.musseukpeople.woorimap.event.application;

import com.musseukpeople.woorimap.event.domain.PostEvent;

public interface Publisher {

    void publish(PostEvent postEvent);
}
