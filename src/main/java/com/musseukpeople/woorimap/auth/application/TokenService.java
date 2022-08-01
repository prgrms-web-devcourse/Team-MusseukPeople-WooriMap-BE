package com.musseukpeople.woorimap.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.Token;
import com.musseukpeople.woorimap.auth.domain.TokenRepository;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveToken(String memberId, String refreshToken, long refreshTokenExpiredTime) {
        Token token = new Token(memberId, refreshToken, refreshTokenExpiredTime);
        tokenRepository.save(token);
    }

    @Transactional
    public void removeByMemberId(String memberId) {
        tokenRepository.deleteById(memberId);
    }

    public Token getTokenByMemberId(String memberId) {
        return tokenRepository.findById(memberId)
            .orElseThrow(() -> new InvalidTokenException(null, ErrorCode.INVALID_TOKEN));
    }
}
