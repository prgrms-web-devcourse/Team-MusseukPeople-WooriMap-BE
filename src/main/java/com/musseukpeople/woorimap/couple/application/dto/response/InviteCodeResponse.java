package com.musseukpeople.woorimap.couple.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InviteCodeResponse {

    @Schema(description = "초대 코드")
    private String code;

    public InviteCodeResponse(String code) {
        this.code = code;
    }
}
