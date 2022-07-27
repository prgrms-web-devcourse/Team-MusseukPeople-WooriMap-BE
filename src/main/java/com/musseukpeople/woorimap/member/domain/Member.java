package com.musseukpeople.woorimap.member.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.musseukpeople.woorimap.member.domain.vo.Email;
import com.musseukpeople.woorimap.member.domain.vo.NickName;
import com.musseukpeople.woorimap.member.domain.vo.Password;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	@Embedded
	private Password password;

	@Embedded
	private NickName nickName;

	@Column
	private String imageUrl;

	@Builder
	public Member(Email email, Password password, NickName nickName, String imageUrl) {
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		this.imageUrl = imageUrl;
	}

	public void checkPassword(PasswordEncoder passwordEncoder, String inputPassword) {
		if (this.password.isNotSamePassword(passwordEncoder, inputPassword)) {
			throw new LoginFailedException();
		}
	}
}
