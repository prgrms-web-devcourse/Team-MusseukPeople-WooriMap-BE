package com.musseukpeople.woorimap.auth.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.application.AuthService;
import com.musseukpeople.woorimap.auth.application.dto.request.RefreshTokenRequest;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.common.model.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인 API입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<TokenResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authService.login(signInRequest);
        return ResponseEntity.ok(new ApiResponse<>(tokenResponse));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
    @PostMapping("/signout")
    public ResponseEntity<Void> logout(@Login LoginMember loginMember) {
        authService.logout(loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급 받습니다.")
    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshAccessToken(HttpServletRequest httpServletRequest,
                                                                               @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String accessToken = getAccessTokenByRequest(httpServletRequest);
        AccessTokenResponse refreshAccessToken = authService.refreshAccessToken(accessToken, refreshTokenRequest);
        return ResponseEntity.ok(new ApiResponse<>(refreshAccessToken));
    }

    private String getAccessTokenByRequest(HttpServletRequest httpServletRequest) {
        return AuthorizationExtractor.extract(httpServletRequest)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
    }
}
