package com.musseukpeople.woorimap.inviteCode.domain;

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
    private LocalDateTime expireDate;

    public InviteCode(String code, Long inviterId) {
        this(code, inviterId, LocalDateTime.now().plusDays(1));
    }

    public InviteCode(String code, Long inviterId, LocalDateTime expireDate) {
        validateCode(code);
        this.code = code;
        this.inviterId = inviterId;
        this.expireDate = expireDate;
    }

    private void validateCode(String code) {
        checkArgument(code.length() == 7, "초대 코드의 길이가 다릅니다.");
        checkArgument(code.matches(CODE_PATTERN), "초대 코드가 숫자가 아닙니다.");
    }
}
