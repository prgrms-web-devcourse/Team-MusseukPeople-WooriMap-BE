package com.musseukpeople.woorimap.image.util;

import java.io.File;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static boolean removeFile(File file) {
        return file.delete();
    }
}
