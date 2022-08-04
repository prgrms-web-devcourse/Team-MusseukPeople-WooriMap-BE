package com.musseukpeople.woorimap.common.validator;

import java.io.IOException;
import java.net.URL;

import org.apache.tika.Tika;

import com.musseukpeople.woorimap.common.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageUtil {

    private static final String IMAGE_MIMETYPE = "image";
    private static Tika tika = new Tika();

    public static boolean isImageUrl(String url) {
        try {
            String mimeType = tika.detect(new URL(url));
            return mimeType.startsWith(IMAGE_MIMETYPE);
        } catch (IOException e) {
            throw new InvalidImageUrlExcpetion(url, ErrorCode.INVALID_IMAGE_URL);
        }
    }
}
