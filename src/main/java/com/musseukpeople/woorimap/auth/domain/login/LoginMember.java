package com.musseukpeople.woorimap.auth.domain.login;

import lombok.Getter;

@Getter
public class LoginMember {

    private final Long id;
    private final Long coupleId;

    public LoginMember(Long id, Long coupleId) {
        this.id = id;
        this.coupleId = coupleId;
    }

    public boolean isCouple() {
        return this.coupleId != null;
    }
}
