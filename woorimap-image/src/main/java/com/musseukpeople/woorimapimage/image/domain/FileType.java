package com.musseukpeople.woorimapimage.image.domain;

import java.util.Arrays;

public enum FileType {
    JPEG,
    JPG,
    GIF,
    PNG,
    BMP;

    public static boolean verifyType(String extension) {
        return Arrays.stream(FileType.values())
            .map(Enum::name)
            .anyMatch(e -> e.equals(extension.toUpperCase()));
    }
}
