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
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String id;
    private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    public RefreshToken(String id, String value, long expiredTime) {
        this.id = id;
        this.value = value;
        this.expiredTime = expiredTime;
    }

    public boolean isNotSame(String refreshToken) {
        return !Objects.equals(this.value, refreshToken);
    }
}
