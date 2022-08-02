package com.musseukpeople.woorimap.auth.aop;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.*;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class MemberAuthorityContext {

    private Authority authority;

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
