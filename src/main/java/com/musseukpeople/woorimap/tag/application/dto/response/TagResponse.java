package com.musseukpeople.woorimap.tag.application.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagResponse {

    private String name;
    private String color;

    public TagResponse(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
