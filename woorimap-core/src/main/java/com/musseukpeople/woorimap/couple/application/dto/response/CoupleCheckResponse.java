package com.musseukpeople.woorimap.couple.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleCheckResponse {

    @Schema(description = "토큰")
    private String accessToken;

    @Schema(description = "커플 유무")
    private Boolean isCouple;

    public CoupleCheckResponse(String accessToken, boolean isCouple) {
        this.accessToken = accessToken;
        this.isCouple = isCouple;
    }
}
