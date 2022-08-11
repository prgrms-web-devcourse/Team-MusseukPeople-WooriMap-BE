package com.musseukpeople.woorimap.event.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.musseukpeople.woorimap.post.domain.Post;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostEvent implements Serializable {

    private static final long serialVersionUID = 5576120921802674645L;

    private Long sourceId;
    private Long destinationId;
    private Long postId;
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime publishDateTime;

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
