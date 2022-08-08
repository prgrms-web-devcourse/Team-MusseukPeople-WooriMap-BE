package com.musseukpeople.woorimap.tag.application.dto.request;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagRequest {

    @Schema(description = "태그 이름")
    @NotBlank
    private String name;

    @Schema(description = "태그 색깔")
    @NotBlank
    private String color;

    public TagRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
