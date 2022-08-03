package com.musseukpeople.woorimap.post.application.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.entity.Post;
import com.musseukpeople.woorimap.post.entity.PostImage;

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
    private List<String> imagePaths;

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
    public CreatePostRequest(String title, String content, List<String> imagePaths, List<TagRequest> tags, BigDecimal latitude,
                             BigDecimal longitude) {
        this.title = title;
        this.content = content;
        this.imagePaths = imagePaths;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Post toPost(Couple coupleId) {
        Post post = Post.builder()
            .couple(coupleId)
            .title(title)
            .content(content)
            .latitude(latitude)
            .longitude(longitude)
            .postImages(toPostImages())
            .build();
        return post;
    }

    public List<PostImage> toPostImages() {
        List<PostImage> postImageList = new ArrayList<>();
        for (String imagePath : imagePaths) {
            postImageList.add(new PostImage(imagePath));
        }
        return postImageList;
    }
}
