package com.musseukpeople.woorimap.couple.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;
import com.musseukpeople.woorimap.couple.exception.NotFoundCoupleException;
import com.musseukpeople.woorimap.member.domain.Member;

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

    @Transactional
    public Long createCouple(List<Member> members, LocalDate createCoupleDate) {
        CoupleMembers coupleMembers = new CoupleMembers(members);
        Couple couple = coupleRepository.save(new Couple(createCoupleDate, coupleMembers));
        return couple.getId();
    }

    public Couple getCoupleByIdFetchMember(Long coupleId) {
        return coupleRepository.findByIdFetchMember(coupleId)
            .orElseThrow(
                () -> new NotFoundCoupleException(ErrorCode.NOT_FOUND_COUPLE, coupleId)
            );
    }
}
