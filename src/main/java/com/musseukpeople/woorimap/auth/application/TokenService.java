package com.musseukpeople.woorimap.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.Token;
import com.musseukpeople.woorimap.auth.domain.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public void saveToken(String refreshToken, Long memberId) {
        Token token = new Token(refreshToken, memberId);
        tokenRepository.save(token);
    }

    @Transactional
    public void removeByMemberId(Long memberId) {
        tokenRepository.deleteByMemberId(memberId);
    }
}
