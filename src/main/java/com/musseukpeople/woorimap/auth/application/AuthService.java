package com.musseukpeople.woorimap.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.LoginMemberResponse;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.domain.Member;

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
    public TokenResponse login(SignInRequest signInRequest) {
        Member member = memberService.getMemberByEmail(signInRequest.getEmail());
        member.checkPassword(passwordEncoder, signInRequest.getPassword());

        Long memberId = member.getId();
        Long coupleId = member.isCouple() ? member.getCouple().getId() : null;
        String accessToken = jwtProvider.createAccessToken(String.valueOf(memberId), coupleId);
        String refreshToken = jwtProvider.createRefreshToken(String.valueOf(memberId));
        tokenService.saveToken(refreshToken, memberId);
        return new TokenResponse(accessToken, refreshToken, LoginMemberResponse.from(member));
    }
}
