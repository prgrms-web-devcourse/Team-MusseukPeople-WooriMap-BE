package com.musseukpeople.woorimap.couple.application.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCoupleRequest {

    @Schema(description = "커플 코드")
    @NotBlank(message = "커플 코드를 입력해 주세요")
    @Size(min = 7, max = 7, message = "코드 길이가 올바르지 않습니다")
    private String code;

    public CreateCoupleRequest(String code) {
        this.code = code;
    }
}
