package com.musseukpeople.woorimap.couple.application.dto.response;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.exception.MappingCoupleMemberException;
import com.musseukpeople.woorimap.member.domain.Member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoupleMyResponse {

    @Schema(description = "프로필 사진 경로")
    private String imageUrl;

    @Schema(description = "닉네임")
    private String nickName;

    public CoupleMyResponse(String imageUrl, String nickName) {
        this.imageUrl = imageUrl;
        this.nickName = nickName;
    }

    public static CoupleMyResponse from(Couple couple, Long myId) {
        return couple.getCoupleMembers().getMembers().stream()
            .filter(member -> member.isSame(myId))
            .findAny()
            .map(CoupleMyResponse::from)
            .orElseThrow(
                () -> new MappingCoupleMemberException("나의 정보를 변환할 수 없습니다.", ErrorCode.NOT_MAPPING_COUPLE_MEMBER));
    }

    public static CoupleMyResponse from(Member member) {
        return new CoupleMyResponse(member.getImageUrl(), member.getNickName().getValue());
    }
}
