package com.musseukpeople.woorimap.couple.application.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleEditResponse {

    @Schema(description = "수정된 날짜")
    private LocalDate startDate;

    public CoupleEditResponse(LocalDate startDate) {
        this.startDate = startDate;
    }
}
