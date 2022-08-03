package com.musseukpeople.woorimap.member.application.dto.response;

import java.time.LocalDate;

import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description = "프로필 이미지 url")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    @Schema(description = "커플 상대방 닉네임", nullable = true)
    private String coupleNickName;

    @Schema(description = "커플 시작 날짜", nullable = true)
    private LocalDate coupleStartingDate;

    @Schema(description = "커플 유무")
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
