package com.musseukpeople.woorimap.auth.application.dto.response;

import lombok.Getter;

@Getter
public class MemberResponse {

    private final String email;
    private final String nickName;

    public MemberResponse(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }
}
