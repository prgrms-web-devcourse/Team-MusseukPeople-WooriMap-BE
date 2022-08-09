package com.musseukpeople.woorimap.tag.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostTagResponse {

    @Schema(description = "태그 이름")
    private String name;

    @Schema(description = "태그 색깔")
    private String color;

    public PostTagResponse(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
