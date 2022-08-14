package com.musseukpeople.woorimap.auth.application;

import java.util.Date;
import java.util.Objects;

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
import com.musseukpeople.woorimap.auth.exception.InvalidTokenRequestException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.notification.application.NotificationService;

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

        TokenDto accessToken = createAccessToken(member);
        TokenDto refreshToken = jwtProvider.createRefreshToken();

        refreshTokenService.saveToken(String.valueOf(member.getId()), refreshToken);
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
        String memberId = jwtProvider.getSubject(accessToken);
        Long coupleId = jwtProvider.getCoupleId(accessToken);
        validateRefreshToken(memberId, refreshToken);

        String newAccessToken = createNewAccessToken(memberId, coupleId);
        return new AccessTokenResponse(newAccessToken);
    }

    @Transactional
    public AccessTokenResponse refreshCoupleAccessToken(String accessToken, String refreshToken) {
        String memberId = jwtProvider.getSubject(accessToken);
        Long coupleId = jwtProvider.getCoupleId(accessToken);
        validateRefreshToken(memberId, refreshToken);

        Member member = memberService.getMemberWithCoupleById(Long.parseLong(memberId));
        validateCoupleStatus(member, coupleId);
        TokenDto newAccessToken = createAccessToken(member);
        registerBlackList(accessToken);
        return new AccessTokenResponse(newAccessToken.getValue());
    }

    private void validateCoupleStatus(Member member, Long coupleId) {
        if (member.isCouple() && Objects.nonNull(coupleId)) {
            throw new InvalidTokenRequestException(ErrorCode.INVALID_TOKEN_REQUEST);
        }

        if (!member.isCouple() && Objects.isNull(coupleId)) {
            throw new InvalidTokenRequestException(ErrorCode.INVALID_TOKEN_REQUEST);
        }
    }

    private TokenDto createAccessToken(Member member) {
        String memberId = String.valueOf(member.getId());
        Long coupleId = member.isCouple() ? member.getCouple().getId() : null;
        return jwtProvider.createAccessToken(memberId, coupleId);
    }

    private void registerBlackList(String accessToken) {
        Date expiredDate = jwtProvider.getExpiredDate(accessToken);
        TokenDto tokenDto = new TokenDto(accessToken, expiredDate.getTime() - new Date().getTime());

        blackListService.saveBlackList(tokenDto);
    }

    private void validateRefreshToken(String memberId, String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }

        RefreshToken findRefreshToken = getRefreshToken(memberId);
        if (findRefreshToken.isNotSame(refreshToken)) {
            throw new InvalidTokenException(refreshToken, ErrorCode.INVALID_TOKEN);
        }
    }

    private RefreshToken getRefreshToken(String memberId) {
        return refreshTokenService.getTokenByMemberId(memberId);
    }

    private String createNewAccessToken(String memberId, Long coupleId) {
        return jwtProvider.createAccessToken(memberId, coupleId).getValue();
    }
}
