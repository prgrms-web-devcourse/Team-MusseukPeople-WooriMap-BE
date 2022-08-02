package com.musseukpeople.woorimap.couple.domain.vo;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.member.domain.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class CoupleMembers {

    private static final int COUPLE_MEMBERS_SIZE = 2;

    @OneToMany(mappedBy = "couple")
    private List<Member> members = new ArrayList<>();

    public CoupleMembers(List<Member> members) {
        validateCoupleMembers(members);
        this.members = members;
    }

    private void validateCoupleMembers(List<Member> members) {
        checkArgument(members.size() == COUPLE_MEMBERS_SIZE, "커플 인원이 2명이 아닙니다");
    }
}
