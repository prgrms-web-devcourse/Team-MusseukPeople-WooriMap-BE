package com.musseukpeople.woorimap.couple.application.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleResponse {

    private LocalDate startDate;
    private CoupleMyResponse me;
    private CoupleYourResponse you;

    public CoupleResponse(LocalDate startDate,
                          CoupleMyResponse coupleMyResponse,
                          CoupleYourResponse coupleYourResponse) {
        this.startDate = startDate;
        this.me = coupleMyResponse;
        this.you = coupleYourResponse;
    }
}
