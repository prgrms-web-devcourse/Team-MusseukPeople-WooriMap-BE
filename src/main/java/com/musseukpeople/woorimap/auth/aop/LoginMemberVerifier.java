package com.musseukpeople.woorimap.auth.aop;

import static com.musseukpeople.woorimap.auth.domain.login.LoginMember.*;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.auth.exception.MemberAccessDeniedExcpetion;
import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginMemberVerifier {

    private final MemberAuthorityContext memberAuthorityContext;

    @Before("@annotation(com.musseukpeople.woorimap.auth.aop.OnlySolo)")
    public void checkSolo() {
        Authority authority = memberAuthorityContext.getAuthority();
        if (!Authority.SOLO.equals(authority)) {
            throw new MemberAccessDeniedExcpetion(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    @Before("@annotation(com.musseukpeople.woorimap.auth.aop.OnlyCouple)")
    public void checkCouple() {
        Authority authority = memberAuthorityContext.getAuthority();
        if (!Authority.COUPLE.equals(authority)) {
            throw new MemberAccessDeniedExcpetion(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }
}
