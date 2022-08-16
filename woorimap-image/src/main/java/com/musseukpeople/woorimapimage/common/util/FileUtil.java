package com.musseukpeople.woorimapimage.common.util;

import java.io.File;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    public static boolean removeFile(File file) {
        return file.delete();
    }
}
