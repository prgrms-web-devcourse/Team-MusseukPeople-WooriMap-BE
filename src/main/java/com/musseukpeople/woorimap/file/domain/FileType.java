package com.musseukpeople.woorimap.file.domain;

import static com.musseukpeople.woorimap.common.exception.ErrorCode.*;

import java.util.Arrays;

import com.musseukpeople.woorimap.file.exception.NotSupportImageException;

public enum FileType {
    JPEG,
    JPG,
    GIF,
    PNG,
    BMP;

    public static void verifyType(String extension) {
        Arrays.stream(FileType.values())
            .map(Enum::name)
            .filter(e -> e.equals(extension.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new NotSupportImageException(extension, INVALID_IMAGE_EXTENSION));
    }
}
