package com.musseukpeople.woorimap.member.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.domain.Couple;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Builder
    public Member(Email email, Password password, NickName nickName, String imageUrl) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String inputPassword) {
        if (this.password.isNotSamePassword(passwordEncoder, inputPassword)) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }
    }

    public void changeCouple(Couple couple) {
        this.couple = couple;
    }

    public void breakUp() {
        this.couple = null;
    }
}
