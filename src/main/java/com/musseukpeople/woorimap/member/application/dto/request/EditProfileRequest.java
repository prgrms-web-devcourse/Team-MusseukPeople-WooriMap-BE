package com.musseukpeople.woorimap.member.application.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditProfileRequest {

    @Schema(description = "프로필 이미지 URL")
    private String imageUrl;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
    private String nickName;

    @Schema(description = "커플 시작 날짜")
    private LocalDate coupleStartDate;

    public EditProfileRequest(String imageUrl, String nickName, LocalDate coupleStartDate) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.coupleStartDate = coupleStartDate;
    }
}
