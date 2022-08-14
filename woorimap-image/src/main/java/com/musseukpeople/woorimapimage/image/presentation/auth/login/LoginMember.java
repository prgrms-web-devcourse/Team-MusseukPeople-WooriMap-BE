package com.musseukpeople.woorimapimage.image.presentation.auth.login;

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

    public LoginMember(Long id, Long coupleId, Authority authority, String accessToken) {
        this.id = id;
        this.coupleId = coupleId;
        this.authority = authority;
        this.accessToken = accessToken;
    }

    public enum Authority {
        ANONYMOUS, SOLO, COUPLE
    }
}
