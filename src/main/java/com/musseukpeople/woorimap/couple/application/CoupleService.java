package com.musseukpeople.woorimap.couple.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.exception.NotFoundCoupleException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoupleService {

    private final CoupleRepository coupleRepository;

    @Transactional
    public void removeCouple(Long coupleId) {
        Couple couple = coupleRepository.findById(coupleId)
            .orElseThrow(() -> new NotFoundCoupleException(ErrorCode.NOT_FOUND_COUPLE, coupleId));
        couple.clearMembers();

        coupleRepository.deleteById(coupleId);
    }
}
