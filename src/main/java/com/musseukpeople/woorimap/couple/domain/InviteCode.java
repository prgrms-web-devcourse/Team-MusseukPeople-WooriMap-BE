package com.musseukpeople.woorimap.couple.domain;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InviteCode {

    private static final String CODE_PATTERN = "^\\d+";

    @Id
    private String code;

    @Column(nullable = false, updatable = false)
    private Long inviterId;

    @Column(nullable = false)
    private LocalDateTime expireDateTime;

    public InviteCode(String code, Long inviterId, LocalDateTime expireDateTime) {
        validateCode(code);
        this.code = code;
        this.inviterId = inviterId;
        this.expireDateTime = expireDateTime;
    }

    private void validateCode(String code) {
        checkArgument(code.length() == 7, "초대 코드의 길이가 다릅니다.");
        checkArgument(code.matches(CODE_PATTERN), "초대 코드가 숫자가 아닙니다.");
    }
}
