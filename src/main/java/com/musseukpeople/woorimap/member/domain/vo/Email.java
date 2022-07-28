package com.musseukpeople.woorimap.member.domain.vo;

import static com.google.common.base.Preconditions.*;
import static java.text.MessageFormat.*;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

    private static final String EMAIL_FORMAT_REGEX = "^[_a-z\\d-]+(.[_a-z\\d-]+)*@(?:\\w+\\.)+\\w+";
    private static final Pattern EMAIL_FORMAT_PATTERN = Pattern.compile(EMAIL_FORMAT_REGEX);
    private static final int MAX_EMAIL_LENGTH = 50;

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public Email(String value) {
        validateEmail(value);
        this.value = value;
    }

    private void validateEmail(String value) {
        checkArgument(!StringUtils.isBlank(value), format("이메일은 비어있을 수 없습니다. 현재 이메일 : {0}", value));
        checkArgument(value.length() <= MAX_EMAIL_LENGTH,
            format("이메일은 {0}글자를 초과할 수 없습니다. 현재 이메일 길이: {1}", MAX_EMAIL_LENGTH, value.length()));
        checkArgument(EMAIL_FORMAT_PATTERN.matcher(value).matches(), format("올바른 이메일 형식이 아닙니다. 현재 이메일 : {0}", value));
    }
}
