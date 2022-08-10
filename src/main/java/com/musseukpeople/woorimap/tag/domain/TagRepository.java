package com.musseukpeople.woorimap.tag.domain;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.infrastructure.QueryTagRepository;

public interface TagRepository extends Repository<Tag, Long>, QueryTagRepository {

    @CacheEvict(
        key = "#p0.iterator().next().couple.id",
        value = "coupleTags",
        condition = "#p0 != null && #p0.iterator().hasNext()"
    )
    void saveAll(Iterable<Tag> tags);

    @Query("SELECT t FROM Tag t WHERE t.couple.id = :coupleId")
    List<Tag> findAllByCoupleId(@Param("coupleId") Long coupleId);

    List<Tag> findExistTagByCouple(Couple couple, List<String> tagNames);
}
