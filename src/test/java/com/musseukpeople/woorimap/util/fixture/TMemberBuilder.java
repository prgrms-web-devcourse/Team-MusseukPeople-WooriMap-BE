package com.musseukpeople.woorimap.util.fixture;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.vo.Email;
import com.musseukpeople.woorimap.member.domain.vo.NickName;
import com.musseukpeople.woorimap.member.domain.vo.Password;

public class TMemberBuilder {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private Email email = new Email("test@gmail.com");
    private Password password = Password.encryptPassword(PASSWORD_ENCODER, "!Test1234");
    private NickName nickName = new NickName("test");
    private String imageUrl = "test";
    private Couple couple = null;

    public TMemberBuilder email(String email) {
        this.email = new Email(email);
        return this;
    }

    public TMemberBuilder password(String password) {
        this.password = Password.encryptPassword(PASSWORD_ENCODER, password);
        return this;
    }

    public TMemberBuilder nickName(String nickName) {
        this.nickName = new NickName(nickName);
        return this;
    }

    public TMemberBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TMemberBuilder couple(Couple couple) {
        this.couple = couple;
        return this;
    }

    public Member build() {
        Member member = Member.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .imageUrl(imageUrl)
            .build();
        member.changeCouple(couple);
        return member;
    }

}
