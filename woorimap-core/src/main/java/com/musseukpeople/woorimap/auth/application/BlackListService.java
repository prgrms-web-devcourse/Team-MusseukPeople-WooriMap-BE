package com.musseukpeople.woorimap.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.domain.BlackList;
import com.musseukpeople.woorimap.auth.domain.BlackListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlackListService {

    private final BlackListRepository blackListRepository;

    @Transactional
    public void saveBlackList(TokenDto tokenDto) {
        BlackList blackList = new BlackList(tokenDto.getValue(), tokenDto.getExpiredTime());
        blackListRepository.save(blackList);
    }

    public boolean isBlackList(String accessToken) {
        return blackListRepository.existsById(accessToken);
    }
}
