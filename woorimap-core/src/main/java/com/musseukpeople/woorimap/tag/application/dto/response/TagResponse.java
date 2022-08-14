package com.musseukpeople.woorimap.tag.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagResponse {

    @Schema(description = "태그 아이디")
    private Long id;

    @Schema(description = "태그 이름")
    private String name;

    @Schema(description = "태그 색깔")
    private String color;

    public TagResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
