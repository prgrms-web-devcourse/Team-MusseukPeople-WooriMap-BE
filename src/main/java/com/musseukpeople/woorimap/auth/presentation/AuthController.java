package com.musseukpeople.woorimap.auth.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.application.AuthService;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.exception.UnauthorizedException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.auth.presentation.dto.response.LoginResponse;
import com.musseukpeople.woorimap.auth.presentation.util.CookieUtil;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.common.model.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인 API입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest,
                                                             HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(signInRequest);
        ResponseCookie cookie = CookieUtil.createTokenCookie(REFRESH_TOKEN_COOKIE_NAME,
            loginResponseDto.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new ApiResponse<>(
            new LoginResponse(loginResponseDto.getAccessToken().getValue(), loginResponseDto.getMember())));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
    @PostMapping("/signout")
    public ResponseEntity<Void> signout(@Login LoginMember loginMember) {
        authService.logout(loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급 받습니다.")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshAccessToken(HttpServletRequest httpServletRequest) {
        String accessToken = getAccessTokenByRequest(httpServletRequest);
        String refreshToken = CookieUtil.getCookieValue(httpServletRequest, REFRESH_TOKEN_COOKIE_NAME);
        AccessTokenResponse refreshAccessToken = authService.refreshAccessToken(accessToken, refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(refreshAccessToken));
    }

    private String getAccessTokenByRequest(HttpServletRequest httpServletRequest) {
        return AuthorizationExtractor.extract(httpServletRequest)
            .orElseThrow(() -> new UnauthorizedException(ErrorCode.NOT_FOUND_TOKEN));
    }
}
