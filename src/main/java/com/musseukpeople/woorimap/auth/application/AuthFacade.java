package com.musseukpeople.woorimap.auth.application;

import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginResponseDto;
import com.musseukpeople.woorimap.auth.domain.RefreshToken;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.notification.application.NotificationService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final BlackListService blackListService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final NotificationService notificationService;

    @Transactional
    public LoginResponseDto login(SignInRequest signInRequest) {
        Member member = memberService.getMemberByEmail(signInRequest.getEmail());
        member.checkPassword(passwordEncoder, signInRequest.getPassword());

        String memberId = String.valueOf(member.getId());
        Long coupleId = member.isCouple() ? member.getCouple().getId() : null;
        TokenDto accessToken = jwtProvider.createAccessToken(memberId, coupleId);
        TokenDto refreshToken = jwtProvider.createRefreshToken();

        refreshTokenService.saveToken(memberId, refreshToken);
        return new LoginResponseDto(accessToken, refreshToken, LoginMemberResponse.from(member));
    }

    @Transactional
    public void logout(LoginMember member) {
        String memberId = String.valueOf(member.getId());
        String accessToken = member.getAccessToken();

        registerBlackList(accessToken);
        refreshTokenService.removeByMemberId(memberId);

        // TODO : 서버 분리 시 API로 변경
        notificationService.deleteAllEmitterByMemberId(memberId);
    }

    public AccessTokenResponse refreshAccessToken(String accessToken, String refreshToken) {
        validateRefreshToken(refreshToken);

        Claims claims = jwtProvider.getClaims(accessToken);
        RefreshToken findRefreshToken = getRefreshToken(claims);
        if (findRefreshToken.isNotSame(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = createNewAccessToken(claims);
        return new AccessTokenResponse(newAccessToken);
    }

    private void registerBlackList(String accessToken) {
        Date expiredDate = jwtProvider.getExpiredDate(accessToken);
        TokenDto tokenDto = new TokenDto(accessToken, expiredDate.getTime() - new Date().getTime());

        blackListService.saveBlackList(tokenDto);
    }

    private void validateRefreshToken(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }
    }

    private RefreshToken getRefreshToken(Claims claims) {
        String memberId = claims.getSubject();
        return refreshTokenService.getTokenByMemberId(memberId);
    }

    private String createNewAccessToken(Claims claims) {
        return jwtProvider.createAccessToken(claims.getSubject(), claims.get(jwtProvider.getClaimName(), Long.class))
            .getValue();
    }
}
