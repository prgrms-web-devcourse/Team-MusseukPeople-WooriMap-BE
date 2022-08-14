package com.musseukpeople.woorimap.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.musseukpeople.woorimap.auth.aop.MemberAuthorityContext;
import com.musseukpeople.woorimap.auth.application.BlackListService;
import com.musseukpeople.woorimap.auth.application.JwtProvider;
import com.musseukpeople.woorimap.auth.presentation.AuthInterceptor;
import com.musseukpeople.woorimap.auth.presentation.resolver.AuthArgumentResolver;
import com.musseukpeople.woorimap.auth.presentation.resolver.JwtTokenArgumentResolver;
import com.musseukpeople.woorimap.notification.presentation.resolver.AuthNotificationArgumentResolver;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    private final JwtProvider jwtProvider;
    private final BlackListService blackListService;
    private final MemberAuthorityContext memberAuthorityContext;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "https://woorimap.vercel.app")
            .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
            .allowCredentials(true)
            .exposedHeaders(HttpHeaders.LOCATION)
            .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtProvider, blackListService));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(jwtProvider, memberAuthorityContext));
        resolvers.add(new AuthNotificationArgumentResolver(jwtProvider, memberAuthorityContext));
        resolvers.add(new JwtTokenArgumentResolver());
    }
}
