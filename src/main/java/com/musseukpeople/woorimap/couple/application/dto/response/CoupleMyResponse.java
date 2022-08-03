package com.musseukpeople.woorimap.couple.application.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleMyResponse {

    private String profileImagePath;
    private String nickName;

    public CoupleMyResponse(String profileImagePath, String nickName) {
        this.profileImagePath = profileImagePath;
        this.nickName = nickName;
    }
}
