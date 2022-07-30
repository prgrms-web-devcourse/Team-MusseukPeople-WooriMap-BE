package com.musseukpeople.woorimap.inviteCode.application.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InviteCodeResponse {

    private String code;

    public InviteCodeResponse(String code) {
        this.code = code;
    }
}
