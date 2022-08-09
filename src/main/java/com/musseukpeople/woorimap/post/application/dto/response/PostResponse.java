package com.musseukpeople.woorimap.post.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.musseukpeople.woorimap.tag.application.dto.response.PostTagResponse;

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

    public PostResponse(Long id, String title, String content, List<String> imageUrls, LocalDate createdDate,
                        LocalDate datingDate, LocationResponse location, List<PostTagResponse> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.createdDate = createdDate;
        this.datingDate = datingDate;
        this.location = location;
        this.tags = tags;
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
    }
}
