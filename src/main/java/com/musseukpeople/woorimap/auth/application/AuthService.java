package com.musseukpeople.woorimap.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.domain.Token;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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

    @Transactional
    public void logout(Long memberId) {
        tokenService.removeByMemberId(String.valueOf(memberId));
    }

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
        return jwtProvider.createAccessToken(claims.getSubject(), claims.get(jwtProvider.getClaimName(), Long.class))
            .getValue();
    }
}
