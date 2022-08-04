package com.musseukpeople.woorimap.couple.application.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleEditResponse {

    private LocalDate startDate;

    public CoupleEditResponse(LocalDate startDate) {
        this.startDate = startDate;
    }
}
