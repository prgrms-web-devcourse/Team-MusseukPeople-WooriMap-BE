package com.musseukpeople.woorimap.auth.infrastructure;

import static org.springframework.http.HttpHeaders.*;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public static Optional<String> extract(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (Strings.isNullOrEmpty(header)) {
            return Optional.empty();
        }
        return extractToken(header);
    }

    private static Optional<String> extractToken(String header) {
        if (header.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            String authHeaderValue = header.substring(BEARER_TYPE.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return Optional.of(authHeaderValue);
        }
        return Optional.empty();
    }
}
