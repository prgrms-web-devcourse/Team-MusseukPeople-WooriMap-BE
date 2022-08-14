package com.musseukpeople.woorimap.notification.presentation.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@LoginRequired
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = "bearer")
public @interface LoginRequiredForNoti {
}
