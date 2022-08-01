package com.musseukpeople.woorimap.auth.domain;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("token")
public class Token {

    @Id
    private String id;
    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    public Token(String id, String refreshToken, long expiredTime) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }

    public boolean isNotSame(String refreshToken) {
        return !Objects.equals(this.refreshToken, refreshToken);
    }
}
