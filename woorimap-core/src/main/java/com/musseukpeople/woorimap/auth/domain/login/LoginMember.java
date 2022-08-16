package com.musseukpeople.woorimap.auth.domain.login;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.Authority.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMember {

    private Long id;
    private Long coupleId;
    private Authority authority;
    private String accessToken;

    public LoginMember(Authority authority) {
        this(null, null, authority, null);
    }

    public LoginMember(Long id, Long coupleId, String accessToken) {
        this(id, coupleId, coupleId != null ? COUPLE : SOLO, accessToken);
    }

    private LoginMember(Long id, Long coupleId, Authority authority, String accessToken) {
        this.id = id;
        this.coupleId = coupleId;
        this.authority = authority;
        this.accessToken = accessToken;
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
