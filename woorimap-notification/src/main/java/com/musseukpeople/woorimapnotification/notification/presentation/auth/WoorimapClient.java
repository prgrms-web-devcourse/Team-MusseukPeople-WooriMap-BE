package com.musseukpeople.woorimapnotification.notification.presentation.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.musseukpeople.woorimapnotification.common.model.ApiResponse;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.login.LoginMember;

@Component
public class WoorimapClient {

    private final String woorimapUrl;

    public WoorimapClient(
        @Value("${woorimap.api.host}") String woorimapHost,
        @Value("${woorimap.api.port}") String woorimapPort
    ) {
        this.woorimapUrl = woorimapHost + woorimapPort;
    }

    public ApiResponse<LoginMember> getLoginMember(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse<LoginMember>> response = new RestTemplate().exchange(
            woorimapUrl + "/api/auth/login/members", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
            });
        return response.getBody();
    }
}
