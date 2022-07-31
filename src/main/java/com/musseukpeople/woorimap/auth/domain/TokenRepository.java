package com.musseukpeople.woorimap.auth.domain;

import java.util.Optional;

public interface TokenRepository {

    void save(Token token);

    Optional<String> findRefreshTokenByMemberId(String memberId);

    void deleteByMemberId(String memberId);
}
