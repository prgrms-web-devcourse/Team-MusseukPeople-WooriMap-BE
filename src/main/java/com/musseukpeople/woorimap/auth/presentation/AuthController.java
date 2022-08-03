package com.musseukpeople.woorimap.auth.presentation;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.application.AuthFacade;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.presentation.dto.response.LoginResponse;
import com.musseukpeople.woorimap.auth.presentation.resolver.JwtToken;
import com.musseukpeople.woorimap.auth.presentation.resolver.RequestTokens;
import com.musseukpeople.woorimap.auth.presentation.util.CookieUtil;
import com.musseukpeople.woorimap.common.model.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @Operation(summary = "로그인", description = "로그인 API입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest,
                                                             HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authFacade.login(signInRequest);
        setTokenCookie(response, loginResponseDto.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(LoginResponse.from(loginResponseDto)));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
    @LoginRequired
    @PostMapping("/signout")
    public ResponseEntity<Void> signout(@Login LoginMember loginMember) {
        authFacade.logout(loginMember);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급 받습니다.")
    @SecurityRequirement(name = "bearer")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshAccessToken(@RequestTokens JwtToken jwtToken) {
        AccessTokenResponse accessToken = authFacade.refreshAccessToken(
            jwtToken.getAccessToken(),
            jwtToken.getRefreshToken()
        );
        return ResponseEntity.ok(new ApiResponse<>(accessToken));
    }

    private void setTokenCookie(HttpServletResponse response, TokenDto tokenDto) {
        ResponseCookie cookie = CookieUtil.createTokenCookie(tokenDto);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
