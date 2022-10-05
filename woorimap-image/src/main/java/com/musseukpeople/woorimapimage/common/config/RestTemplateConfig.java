package com.musseukpeople.woorimapimage.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class RestTemplateConfig {

    private static final int CONNECT_TIME_OUT_SECONDS = 5;
    private static final int READ_TIME_OUT_SECONDS = 5;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(CONNECT_TIME_OUT_SECONDS))
            .setReadTimeout(Duration.ofSeconds(READ_TIME_OUT_SECONDS))
            .additionalInterceptors(new LogginInterceptor())
            .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
            .build();
    }

    @Slf4j
    static class LogginInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution execution) throws
            IOException {
            printRequest(req, body);
            ClientHttpResponse response = execution.execute(req, body);
            printResponse(response);
            return response;
        }

        private void printRequest(HttpRequest req, byte[] body) {
            log.info("URI : {}, Method : {}, Headers : {}, Body : {}",
                req.getURI(), req.getMethod(), req.getHeaders(), new String(body, StandardCharsets.UTF_8));
        }

        private void printResponse(ClientHttpResponse response) throws IOException {
            String body = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

            log.info("Status : {}, Headers : {}, Body: {}",
                response.getStatusCode(), response.getHeaders(), body);
        }
    }

}
