package com.musseukpeople.woorimap.post.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;
import com.musseukpeople.woorimap.tag.domain.Tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePostRequest {

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "내용")
    @NotBlank
    private String content;

    @Schema(description = "이미지 저장 경로 리스트")
    @NotNull
    private List<String> imageUrls;

    @Schema(description = "태그 리스트")
    @NotNull
    private List<TagRequest> tags;

    @Schema(description = "위도")
    @NotNull
    private BigDecimal latitude;

    @Schema(description = "경도")
    @NotNull
    private BigDecimal longitude;

    @Schema(description = "데이트 날짜")
    @NotNull
    private LocalDate datingDate;

    @Builder
    public CreatePostRequest(String title, String content, List<String> imageUrls, List<TagRequest> tags,
                             BigDecimal latitude, BigDecimal longitude, LocalDate datingDate) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datingDate = datingDate;
    }

    public Post toPost(Couple couple, List<Tag> tags) {
        return Post.builder()
            .couple(couple)
            .title(title)
            .content(content)
            .location(new Location(latitude, longitude))
            .datingDate(datingDate)
            .imageUrls(imageUrls)
            .tags(tags)
            .build();
    }
}
