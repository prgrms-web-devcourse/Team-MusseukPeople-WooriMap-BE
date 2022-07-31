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
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Transactional
    public void removeByMemberId(Long memberId) {
        tokenRepository.deleteById(String.valueOf(memberId));
    }

    public Token getTokenByMemberId(String memberId) {
        return tokenRepository.findById(memberId)
            .orElseThrow(() -> new InvalidTokenException(null, ErrorCode.INVALID_TOKEN));
    }
}
