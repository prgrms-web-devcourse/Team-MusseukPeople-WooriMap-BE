package com.musseukpeople.woorimap.couple.domain.vo;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleMyResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleYourResponse;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.exception.MappingCoupleMemberException;
import com.musseukpeople.woorimap.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoupleMembers {

    private static final int COUPLE_MEMBERS_SIZE = 2;

    @OneToMany(mappedBy = "couple")
    private List<Member> members = new ArrayList<>();

    public CoupleMembers(List<Member> members) {
        validateCoupleMembers(members);
        this.members = members;
    }

    public void setCoupleForMembers(Couple couple) {
        this.members.forEach(member -> member.changeCouple(couple));
    }

    public CoupleMyResponse getMyResponse(Long myId) {
        return this.members.stream()
            .filter(member -> Objects.equals(member.getId(), myId))
            .findFirst()
            .map(member -> new CoupleMyResponse(member.getImageUrl(), member.getNickName().getValue()))
            .orElseThrow(
                () -> new MappingCoupleMemberException("나의 정보를 변환할 수 없습니다.", ErrorCode.NOT_MAPPING_COUPLE_MEMBER));
    }

    public CoupleYourResponse getYourResponse(Long myId) {
        return this.members.stream()
            .filter(member -> !Objects.equals(member.getId(), myId))
            .findFirst()
            .map(member -> new CoupleYourResponse(member.getImageUrl(), member.getNickName().getValue()))
            .orElseThrow(
                () -> new MappingCoupleMemberException("상대방의 정보를 변환할 수 없습니다.", ErrorCode.NOT_MAPPING_COUPLE_MEMBER));
    }

    private void validateCoupleMembers(List<Member> members) {
        checkArgument(members.size() == COUPLE_MEMBERS_SIZE, "커플 인원이 2명이 아닙니다");
    }
}
