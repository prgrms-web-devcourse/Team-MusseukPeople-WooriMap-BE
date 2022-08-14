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

    private String sourceNickName;
    private Long destinationId;
    private Long postId;

    private EventType eventType;
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime publishDateTime;

    public PostEvent(String sourceNickName, Long destinationId, Long postId, EventType eventType, String content,
                     LocalDateTime publishDateTime) {
        this.sourceNickName = sourceNickName;
        this.destinationId = destinationId;
        this.postId = postId;
        this.eventType = eventType;
        this.content = content;
        this.publishDateTime = publishDateTime;
    }

    public static PostEvent of(Long sourceId, Post post, EventType eventType) {
        return new PostEvent(
            post.getCouple().getMyMember(sourceId).getNickName().getValue(),
            post.getCouple().getOpponentMember(sourceId).getId(),
            post.getId(),
            eventType,
            post.getContent(),
            LocalDateTime.now()
        );
    }

    public enum EventType {
        POST_CREATED, POST_MODIFIED
    }
}
