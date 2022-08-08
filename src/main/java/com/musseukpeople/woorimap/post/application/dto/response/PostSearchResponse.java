package com.musseukpeople.woorimap.post.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.musseukpeople.woorimap.post.domain.Post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchResponse {

    private Long postId;
    private String postThumbnailPath;
    private String title;
    private LocalDateTime createDateTime;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @Builder
    private PostSearchResponse(Long postId, String postThumbnailPath, String title, LocalDateTime createDateTime,
                               BigDecimal latitude, BigDecimal longitude) {
        this.postId = postId;
        this.postThumbnailPath = postThumbnailPath;
        this.title = title;
        this.createDateTime = createDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<PostSearchResponse> from(List<Post> posts) {
        return posts.stream().map(post -> new PostSearchResponse(
            post.getId(),
            post.getTitle(),
            post.getThumbnailUrl(),
            post.getCreatedDateTime(),
            post.getLocation().getLatitude(),
            post.getLocation().getLongitude()
        )).collect(Collectors.toList());
    }
}
