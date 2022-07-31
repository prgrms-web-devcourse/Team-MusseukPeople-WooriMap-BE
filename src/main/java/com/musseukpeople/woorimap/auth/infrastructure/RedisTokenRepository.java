package com.musseukpeople.woorimap.auth.infrastructure;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.auth.domain.Token;
import com.musseukpeople.woorimap.auth.domain.TokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void save(Token token) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(
            token.getId(),
            token.getRefreshToken(),
            token.getExpiredTime(),
            TimeUnit.MILLISECONDS
        );
    }

    @Override
    public Optional<String> findRefreshTokenByMemberId(String memberId) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(memberId));
    }

    @Override
    public void deleteByMemberId(String memberId) {
        stringRedisTemplate.delete(memberId);
    }
}
