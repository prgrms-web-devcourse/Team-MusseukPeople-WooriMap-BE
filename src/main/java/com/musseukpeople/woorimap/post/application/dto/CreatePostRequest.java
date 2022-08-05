package com.musseukpeople.woorimap.post.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostImage;
import com.musseukpeople.woorimap.post.domain.PostTag;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequest {

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "내용")
    @NotNull
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

    @Builder
    public CreatePostRequest(String title, String content, List<String> imageUrls, List<TagRequest> tags,
                             BigDecimal latitude, BigDecimal longitude) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Post toPost(Couple coupleId, List<PostTag> postTagIdList) {
        return Post.builder()
            .couple(coupleId)
            .title(title)
            .content(content)
            .location(new Location(latitude, longitude))
            .postImages(toPostImages())
            .postTags(postTagIdList)
            .build();
    }

    public List<PostImage> toPostImages() {
        return imageUrls.stream().map(PostImage::new)
            .collect(Collectors.toList());
    }
}
