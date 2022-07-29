package com.musseukpeople.woorimap.member.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.musseukpeople.woorimap.member.domain.vo.Email;
import com.musseukpeople.woorimap.member.domain.vo.NickName;
import com.musseukpeople.woorimap.member.domain.vo.Password;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;

class MemberTest {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("멤버 생성 성공")
    @Test
    void createMember_success() {
        // given
        String email = "test@mail.com";
        String password = "!@Password123";
        String nickName = "woorimap";

        // when
        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName(nickName))
            .build();

        // then
        assertThat(member).isNotNull();
    }

    @DisplayName("비밀번호 일치 성공")
    @Test
    void checkPassword_success() {
        // given
        String password = "!@Password123";
        Member member = Member.builder()
            .email(new Email("test@mail.com"))
            .password(Password.encryptPassword(passwordEncoder, password))
            .nickName(new NickName("woorimap"))
            .build();

        // when
        // then
        assertDoesNotThrow(() -> member.checkPassword(passwordEncoder, password));
    }

    @DisplayName("일치하지 않는 비밀번호로 인한 실패")
    @Test
    void checkPassword_notMatchPassword_fail() {
        // given
        Member member = Member.builder()
            .email(new Email("test@mail.com"))
            .password(Password.encryptPassword(passwordEncoder, "!@Password123"))
            .nickName(new NickName("woorimap"))
            .build();
        String inputPassword = "!@password1";

        // when
        // then
        assertThatThrownBy(() -> member.checkPassword(passwordEncoder, inputPassword))
            .isInstanceOf(LoginFailedException.class);
    }
}
