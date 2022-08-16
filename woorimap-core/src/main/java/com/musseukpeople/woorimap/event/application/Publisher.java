package com.musseukpeople.woorimap.event.application;

import com.musseukpeople.woorimap.event.domain.PostEvent;

public interface Publisher {

    void publishPost(PostEvent postEvent);

    void publishLogout(String memberId);
}
