package com.musseukpeople.woorimap.util.fixture;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public Member build() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .imageUrl(imageUrl)
            .build();
    }

}
