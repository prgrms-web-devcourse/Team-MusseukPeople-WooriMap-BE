package com.musseukpeople.woorimap.event.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.musseukpeople.woorimap.post.domain.Post;

import lombok.Getter;

@Getter
public class PostEvent implements Serializable {

    private static final long serialVersionUID = 5576120921802674645L;

    private final Long sourceId;
    private final Long destinationId;
    private final Long postId;
    private final String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime publishDateTime;

    public PostEvent(Long sourceId, Long destinationId, Long postId, String content, LocalDateTime publishDateTime) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.postId = postId;
        this.content = content;
        this.publishDateTime = publishDateTime;
    }

    public static PostEvent of(Long sourceId, Post post) {
        return new PostEvent(
            sourceId,
            post.getCouple().getOpponentMember(sourceId).getId(),
            post.getId(),
            post.getContent(),
            LocalDateTime.now()
        );
    }
}
