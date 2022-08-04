package com.musseukpeople.woorimap.couple.application.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleResponse {

    private LocalDate startDate;
    private CoupleMemeberResponse me;
    private CoupleMemeberResponse you;

    public CoupleResponse(LocalDate startDate,
                          CoupleMemeberResponse coupleMeResponse,
                          CoupleMemeberResponse coupleYouResponse) {
        this.startDate = startDate;
        this.me = coupleMeResponse;
        this.you = coupleYouResponse;
    }
}
