package com.musseukpeople.woorimap.post.application.dto.response;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.tag.PostTag;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.domain.Tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private LocationResponse location;
    private List<PostTagResponse> tags;
    private LocalDate datingDate;
    private LocalDate createdDate;

    public PostResponse(Long id, String title, String content, List<String> imageUrls, LocationResponse location,
                        List<PostTagResponse> tags, LocalDate createdDate, LocalDate datingDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.location = location;
        this.tags = tags;
        this.datingDate = datingDate;
        this.createdDate = createdDate;
    }

    public static PostResponse from(Post post) {
        List<PostTagResponse> tags = toPostTagResponse(post);

        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getImageUrls(),
            LocationResponse.from(post.getLocation()),
            tags,
            post.getCreatedDateTime().toLocalDate(),
            post.getDatingDate()
        );
    }

    private static List<PostTagResponse> toPostTagResponse(Post post) {
        return post.getPostTags().getPostTags().stream()
            .map(PostTag::getTag)
            .map(PostTagResponse::from)
            .collect(toList());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class LocationResponse {
        private BigDecimal latitude;
        private BigDecimal longitude;

        public LocationResponse(BigDecimal latitude, BigDecimal longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public static LocationResponse from(Location location) {
            return new LocationResponse(
                location.getLatitude(),
                location.getLongitude()
            );
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class PostTagResponse {

        @Schema(description = "태그 이름")
        private String name;

        @Schema(description = "태그 색깔")
        private String color;

        public PostTagResponse(String name, String color) {
            this.name = name;
            this.color = color;
        }

        public static PostTagResponse from(Tag tag) {
            return new PostTagResponse(tag.getName(), tag.getColor());
        }
    }
}
