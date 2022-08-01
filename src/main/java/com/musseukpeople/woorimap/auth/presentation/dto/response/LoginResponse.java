package com.musseukpeople.woorimap.auth.presentation.dto.response;

import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "멤버 정보")
    private LoginMemberResponse member;

    public LoginResponse(String accessToken, LoginMemberResponse loginMemberResponse) {
        this.accessToken = accessToken;
        this.member = loginMemberResponse;
    }

    public static LoginResponse from(LoginResponseDto loginResponseDto) {
        return new LoginResponse(
            loginResponseDto.getAccessToken().getValue(),
            loginResponseDto.getMember()
        );
    }
}



