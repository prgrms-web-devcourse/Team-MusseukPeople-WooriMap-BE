package com.musseukpeople.woorimap.auth.presentation.fake;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
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
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.domain.Token;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.auth.infrastructure.AuthorizationExtractor;
import com.musseukpeople.woorimap.auth.infrastructure.JwtTokenProvider;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fake")
@RequiredArgsConstructor
public class FakeAuthController {

    private final FakeAuthService authService;

    @Operation(summary = "테스트 로그인", description = "테스트 로그인 API입니다.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<FakeTokenDto>> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        LoginResponseDto loginResponseDto = authService.login(signInRequest);
        return ResponseEntity.ok(new ApiResponse<>(FakeTokenDto.from(loginResponseDto)));
    }

    @Operation(summary = "엑세스 토큰 재발급", description = "엑세스 토큰을 재발급 받습니다.")
    @SecurityRequirement(name = "bearer")
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> refreshAccessToken(HttpServletRequest request,
                                                                               @RequestBody FakeRefreshTokenRequest refreshTokenRequest) {
        AccessTokenResponse accessToken = authService.refreshAccessToken(
            AuthorizationExtractor.extract(request),
            refreshTokenRequest.getRefreshToken()
        );
        return ResponseEntity.ok(new ApiResponse<>(accessToken));
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

        @Transactional(readOnly = true)
        public AccessTokenResponse refreshAccessToken(String accessToken, String refreshToken) {
            if (!jwtProvider.validateToken(refreshToken)) {
                throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
            }

            Claims claims = jwtProvider.getClaims(accessToken);
            Token findRefreshToken = getRefreshToken(claims);
            if (findRefreshToken.isNotSame(refreshToken)) {
                throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
            }

            String newAccessToken = createNewAccessToken(claims);
            return new AccessTokenResponse(newAccessToken);
        }

        private Token getRefreshToken(Claims claims) {
            String memberId = claims.getSubject();
            return tokenService.getTokenByMemberId(memberId);
        }

        private String createNewAccessToken(Claims claims) {
            return jwtProvider.createAccessToken(claims.getSubject(),
                    claims.get(jwtProvider.getClaimName(), Long.class))
                .getValue();
        }
    }

    @Getter
    static class FakeTokenDto {
        private String accessToken;
        private String refreshToken;

        private LoginMemberResponse member;

        public FakeTokenDto(String accessToken, String refreshToken, LoginMemberResponse member) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.member = member;
        }

        public static FakeTokenDto from(LoginResponseDto loginResponseDto) {
            return new FakeTokenDto(
                loginResponseDto.getAccessToken().getValue(),
                loginResponseDto.getRefreshToken().getValue(),
                loginResponseDto.getMember()
            );
        }
    }

    @Getter
    static class FakeRefreshTokenRequest {
        private String refreshToken;

        public FakeRefreshTokenRequest() {
        }

        public FakeRefreshTokenRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
