package com.musseukpeople.woorimapimage.image.presentation.auth.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long id;
    private String email;
    private String imageUrl;
    private String nickName;
    private Boolean isCouple;

    public MemberResponse(Long id, String email, String imageUrl, String nickName, boolean isCouple) {
        this.id = id;
        this.email = email;
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.isCouple = isCouple;
    }
}
