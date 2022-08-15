package com.musseukpeople.woorimapnotification.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearer";
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme(securitySchemeName)
                    .bearerFormat("JWT")
                    .in(SecurityScheme.In.HEADER)
                    .name(HttpHeaders.AUTHORIZATION)))
            .info(new Info().title("WooriMap-notification"));
    }

}
