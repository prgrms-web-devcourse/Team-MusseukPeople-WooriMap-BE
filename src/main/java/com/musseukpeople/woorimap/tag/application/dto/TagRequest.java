package com.musseukpeople.woorimap.tag.application.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagRequest {

    @NotBlank
    private String name;

    private String color;

    public TagRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
