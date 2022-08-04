package com.musseukpeople.woorimap.tag.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.musseukpeople.woorimap.tag.infrastructure.QueryTagRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, QueryTagRepository {
}
