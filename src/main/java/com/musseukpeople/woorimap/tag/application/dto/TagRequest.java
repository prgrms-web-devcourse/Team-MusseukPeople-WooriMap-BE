package com.musseukpeople.woorimap.tag.application.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagRequest {

    private Long id;

    @NotBlank
    private String name;

    private String color;

    public TagRequest(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
