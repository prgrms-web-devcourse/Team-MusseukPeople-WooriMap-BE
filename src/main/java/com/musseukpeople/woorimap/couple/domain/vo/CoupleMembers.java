package com.musseukpeople.woorimap.couple.domain.vo;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.couple.domain.Couple;
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

    public void assignCoupleForMembers(Couple couple) {
        this.members.forEach(member -> member.changeCouple(couple));
    }

    private void validateCoupleMembers(List<Member> members) {
        checkArgument(members.size() == COUPLE_MEMBERS_SIZE, "커플 인원이 2명이 아닙니다");
    }
}
