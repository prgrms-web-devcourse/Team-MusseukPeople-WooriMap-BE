package com.musseukpeople.woorimap.member.application.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private String imageUrl;
    private String nickName;
    private String coupleNickName;
    private LocalDate coupleStartingDate;
    private boolean isCouple;

    public MemberResponse(String imageUrl, String nickName, String coupleNickName, LocalDate coupleStartingDate,
                          boolean isCouple) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.coupleNickName = coupleNickName;
        this.coupleStartingDate = coupleStartingDate;
        this.isCouple = isCouple;
    }
}
