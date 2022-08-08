package com.musseukpeople.woorimap.post.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
