package com.musseukpeople.woorimap.tag.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.musseukpeople.woorimap.tag.infrastructure.QueryTagRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, QueryTagRepository {

    @Query("SELECT t FROM Tag t WHERE t.couple.id = :coupleId")
    List<Tag> findAllByCoupleId(@Param("coupleId") Long coupleId);
}
