package com.musseukpeople.woorimap.auth.domain;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
