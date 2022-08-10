package com.musseukpeople.woorimap.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.domain.RefreshToken;
import com.musseukpeople.woorimap.auth.domain.RefreshTokenRepository;
import com.musseukpeople.woorimap.auth.exception.InvalidTokenException;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveToken(String memberId, TokenDto tokenDto) {
        RefreshToken refreshToken = new RefreshToken(memberId, tokenDto.getValue(), tokenDto.getExpiredTime());
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void removeByMemberId(String memberId) {
        refreshTokenRepository.deleteById(memberId);
    }

    public RefreshToken getTokenByMemberId(String memberId) {
        return refreshTokenRepository.findById(memberId)
            .orElseThrow(() -> new InvalidTokenException(null, ErrorCode.INVALID_TOKEN));
    }
}
