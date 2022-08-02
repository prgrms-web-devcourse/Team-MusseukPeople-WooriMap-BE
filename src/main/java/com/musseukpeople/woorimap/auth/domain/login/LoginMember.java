package com.musseukpeople.woorimap.auth.domain.login;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.Authority.*;

import lombok.Getter;

@Getter
public class LoginMember {

    private Long id;
    private Long coupleId;
    private Authority authority;

    public LoginMember(Authority authority) {
        this(null, null, authority);
    }

    public LoginMember(Long id, Long coupleId) {
        this(id, coupleId, coupleId != null ? COUPLE : SOLO);
    }

    private LoginMember(Long id, Long coupleId, Authority authority) {
        this.id = id;
        this.coupleId = coupleId;
        this.authority = authority;
    }

    public boolean isAnonymous() {
        return ANONYMOUS.equals(authority);
    }

    public boolean isCouple() {
        return COUPLE.equals(authority);
    }

    public boolean isSolo() {
        return SOLO.equals(authority);
    }

    public enum Authority {
        ANONYMOUS, SOLO, COUPLE
    }
}
