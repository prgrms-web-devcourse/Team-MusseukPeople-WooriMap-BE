package com.musseukpeople.woorimap.post.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.musseukpeople.woorimap.tag.application.dto.request.TagRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditPostRequest {

    @Schema(description = "제목")
    @NotBlank
    private String title;

    @Schema(description = "내용")
    @NotBlank
    private String content;

    @Schema(description = "이미지 저장 경로 리스트")
    @NotNull
    @Size(min = 1, max = 5, message = "이미지는 1개 이상 5개 이하로 등록이 가능합니다")
    private List<String> imageUrls;

    @Schema(description = "태그 리스트")
    @NotNull
    @Size(min = 1, message = "태그는 1개 이상 등록해야합니다")
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
    public EditPostRequest(String title, String content, List<String> imageUrls, List<TagRequest> tags,
                           BigDecimal latitude, BigDecimal longitude, LocalDate datingDate) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.tags = tags;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datingDate = datingDate;
    }
}
