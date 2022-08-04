package com.musseukpeople.woorimap.couple.application.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditCoupleRequest {

    @Schema(description = "수정할 날짜")
    @NotNull(message = "수정 날짜가 없습니다")
    private LocalDate editDate;

    public EditCoupleRequest(LocalDate editDate) {
        this.editDate = editDate;
    }
}
