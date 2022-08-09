package com.musseukpeople.woorimap.post.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.musseukpeople.woorimap.post.domain.Post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSearchResponse {

    @Schema(description = "게시글 아이디")
    private Long postId;

    @Schema(description = "썸네일 사진")
    private String imageUrl;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "작성일")
    private LocalDateTime createDateTime;

    @Schema(description = "위도")
    private BigDecimal latitude;

    @Schema(description = "경도")
    private BigDecimal longitude;

    @Builder
    public PostSearchResponse(Long postId, String imageUrl, String title, LocalDateTime createDateTime,
                              BigDecimal latitude, BigDecimal longitude) {
        this.postId = postId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.createDateTime = createDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static List<PostSearchResponse> from(List<Post> posts) {
        return posts.stream().map(post -> new PostSearchResponse(
            post.getId(),
            post.getThumbnailUrl(),
            post.getTitle(),
            post.getCreatedDateTime(),
            post.getLocation().getLatitude(),
            post.getLocation().getLongitude()
        )).collect(Collectors.toList());
    }
}
