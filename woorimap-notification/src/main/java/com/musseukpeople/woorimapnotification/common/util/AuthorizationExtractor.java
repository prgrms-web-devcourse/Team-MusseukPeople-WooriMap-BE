package com.musseukpeople.woorimapnotification.common.util;

import static org.springframework.http.HttpHeaders.*;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer";
    private static final String TOKEN_PARAMETER_NAME = "token";

    public static String extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(header)) {
            return null;
        }
        return header;
    }

    public static String extractQueryString(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_PARAMETER_NAME);
        return BEARER_TYPE + token;
    }
}
