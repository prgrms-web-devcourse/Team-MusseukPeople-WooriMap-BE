package com.musseukpeople.woorimap.auth.domain;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("blackList")
public class BlackList {

    @Id
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    public BlackList(String accessToken, long expiredTime) {
        this.accessToken = accessToken;
        this.expiredTime = expiredTime;
    }
}
