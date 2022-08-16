package com.musseukpeople.woorimapnotification.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.musseukpeople.woorimapnotification.notification.presentation.auth.AuthArgumentResolver;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.WoorimapClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    private final WoorimapClient woorimapClient;

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
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(woorimapClient));
    }
}
