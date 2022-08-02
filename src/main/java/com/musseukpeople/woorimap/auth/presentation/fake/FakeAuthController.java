package com.musseukpeople.woorimap.auth.presentation.fake;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.application.TokenService;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.infrastructure.JwtTokenProvider;
import com.musseukpeople.woorimap.auth.presentation.dto.response.LoginResponse;
import com.musseukpeople.woorimap.auth.presentation.util.CookieUtil;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fake")
@RequiredArgsConstructor
public class FakeAuthController {

    private final FakeAuthService authService;

    @Operation(summary = "테스트 로그인", description = "테스트 로그인 API입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<LoginResponse>> signIn(@Valid @RequestBody SignInRequest signInRequest,
                                                             HttpServletResponse response) {
        LoginResponseDto loginResponseDto = authService.login(signInRequest);
        setTokenCookie(response, loginResponseDto.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse<>(LoginResponse.from(loginResponseDto)));
    }

    private void setTokenCookie(HttpServletResponse response, TokenDto tokenDto) {
        ResponseCookie cookie = CookieUtil.createTokenCookie(tokenDto);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Service
    @Transactional(readOnly = true)
    static class FakeAuthService {
        private final MemberService memberService;
        private final TokenService tokenService;
        private final PasswordEncoder passwordEncoder;
        private JwtProvider jwtProvider;

        public FakeAuthService(MemberService memberService, TokenService tokenService, PasswordEncoder passwordEncoder,
                               @Value("${jwt.issuer}") String issuer, @Value("${jwt.secret-key}") String secretKey) {
            this.memberService = memberService;
            this.tokenService = tokenService;
            this.passwordEncoder = passwordEncoder;
            this.jwtProvider = new JwtTokenProvider(issuer, secretKey, 10_000, 30_000);
        }

        @Transactional
        public LoginResponseDto login(SignInRequest signInRequest) {
            Member member = memberService.getMemberByEmail(signInRequest.getEmail());
            member.checkPassword(passwordEncoder, signInRequest.getPassword());

            String memberId = String.valueOf(member.getId());
            Long coupleId = member.isCouple() ? member.getCouple().getId() : null;
            TokenDto accessToken = jwtProvider.createAccessToken(memberId, coupleId);
            TokenDto refreshToken = jwtProvider.createRefreshToken();

            tokenService.saveToken(memberId, refreshToken);
            return new LoginResponseDto(accessToken, refreshToken, LoginMemberResponse.from(member));
        }
    }
}
