package com.musseukpeople.woorimap.couple.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.domain.CoupleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleService {

    private final CoupleRepository coupleRepository;

    @Transactional
    public void removeCouple(Long coupleId) {
        coupleRepository.deleteById(coupleId);
    }
}
