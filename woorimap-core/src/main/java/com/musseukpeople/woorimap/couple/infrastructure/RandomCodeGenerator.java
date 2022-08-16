package com.musseukpeople.woorimap.couple.infrastructure;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.couple.application.CodeGenerator;

@Component
public class RandomCodeGenerator implements CodeGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int LIMITED_CODE_NUMBER = 10000000;

    @Override
    public String createRandomCode() {
        String randomInviteCodeNumber = String.valueOf(random.nextInt(LIMITED_CODE_NUMBER) + LIMITED_CODE_NUMBER);
        return randomInviteCodeNumber.substring(randomInviteCodeNumber.length() - 7);
    }
}
