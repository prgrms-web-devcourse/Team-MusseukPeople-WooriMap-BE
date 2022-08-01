package com.musseukpeople.woorimap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WoorimapApplication {

    public static void main(String[] args) {
        SpringApplication.run(WoorimapApplication.class, args);
    }

}
