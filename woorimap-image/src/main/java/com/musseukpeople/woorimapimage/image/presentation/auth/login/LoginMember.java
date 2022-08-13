package com.musseukpeople.woorimapimage.image.presentation.auth.login;

import lombok.Getter;

@Getter
public class LoginMember {

    private final Long id;
    private final String accessToken;

    public LoginMember(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
