package com.musseukpeople.woorimap.auth.application.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private String email;
    private String nickName;

    public MemberResponse(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }
}
