package com.musseukpeople.woorimap.post.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.musseukpeople.woorimap.post.infrastructure.PostQueryRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {

    @Query("SELECT p FROM Post p "
        + "JOIN FETCH p.postTags.postTags pt "
        + "JOIN FETCH pt.tag "
        + "JOIN FETCH p.couple "
        + "WHERE p.id = :postId")
    Optional<Post> findPostWithFetchById(@Param("postId") Long postId);
}
