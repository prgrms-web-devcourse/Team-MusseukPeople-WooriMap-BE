package com.musseukpeople.woorimap.member.application.dto.response;

import java.time.LocalDate;

import com.musseukpeople.woorimap.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private String imageUrl;
    private String nickName;
    private String coupleNickName;
    private LocalDate coupleStartingDate;
    private boolean isCouple;

    public MemberResponse(String imageUrl, String nickName, String coupleNickName, LocalDate coupleStartingDate,
                          boolean isCouple) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.coupleNickName = coupleNickName;
        this.coupleStartingDate = coupleStartingDate;
        this.isCouple = isCouple;
    }

    public static MemberResponse createSoloMemberResponse(Member member) {
        return new MemberResponse(
            member.getImageUrl(),
            member.getNickName().getValue(),
            null,
            null,
            member.isCouple()
        );
    }

    public static MemberResponse createCoupleMemberResponse(Member member, Member opponentMember) {
        return new MemberResponse(
            member.getImageUrl(),
            member.getNickName().getValue(),
            opponentMember.getNickName().getValue(),
            member.getCouple().getStartDate(),
            member.isCouple()
        );
    }
}
