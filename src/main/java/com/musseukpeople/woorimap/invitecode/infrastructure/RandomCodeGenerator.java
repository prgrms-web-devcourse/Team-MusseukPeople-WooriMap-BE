package com.musseukpeople.woorimap.invitecode.infrastructure;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.invitecode.application.CodeGenerator;

@Component
public class RandomCodeGenerator implements CodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String createRandomCode() {
        String randomInviteCodeNumber = String.valueOf(random.nextInt(10000000) + 10000000);
        return randomInviteCodeNumber.substring(randomInviteCodeNumber.length() - 7);
    }
}
