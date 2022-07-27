package com.musseukpeople.woorimap.member.domain.vo;

import static com.google.common.base.Preconditions.*;
import static java.text.MessageFormat.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NickName {

	private static final int MAX_NICKNAME_LENGTH = 15;

	@Column(name = "nickname", nullable = false)
	private String value;

	public NickName(String value) {
		validateNickName(value);
		this.value = value;
	}

	private void validateNickName(String value) {
		checkArgument(!StringUtils.isBlank(value), format("닉네임은 비어있을 수 없습니다. nickName = {0}", value));
		checkArgument(value.length() <= MAX_NICKNAME_LENGTH,
			format("닉네임은 {0}글자를 초과할 수 없습니다. nickNameLength = {1}", MAX_NICKNAME_LENGTH, value.length()));
	}
}
