package com.musseukpeople.woorimap.common.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }
}
