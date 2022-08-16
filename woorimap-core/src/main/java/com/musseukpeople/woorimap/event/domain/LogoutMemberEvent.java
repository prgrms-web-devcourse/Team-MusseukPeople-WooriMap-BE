package com.musseukpeople.woorimap.event.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogoutMemberEvent {

    private String memberId;

    public LogoutMemberEvent(String memberId) {
        this.memberId = memberId;
    }
}
