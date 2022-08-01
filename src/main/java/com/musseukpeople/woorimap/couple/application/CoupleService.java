package com.musseukpeople.woorimap.couple.application;

import org.springframework.stereotype.Service;

import com.musseukpeople.woorimap.couple.domain.CoupleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;
}
