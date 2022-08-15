package com.musseukpeople.woorimapimage.image.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.musseukpeople.woorimapimage.common.exception.ErrorCode;
import com.musseukpeople.woorimapimage.image.exception.NotSupportImageException;

import lombok.Getter;

@Getter
public class S3Image {
    private static final String FOLDER_DELIMITER = "/";
    private static final String EXTENSION_DELIMITER = ".";
    private static final String BLANK = " ";
    private static final String BLANK_REPLACEMENT = "+";
    private final MultipartFile multipartFile;
    private final String originFileName;
    private final String extension;

    private S3Image(String originFileName, String extension, MultipartFile multipartFile) {
        if (!FileType.verifyType(extension)) {
            throw new NotSupportImageException(extension, ErrorCode.INVALID_IMAGE_EXTENSION);
        }
        this.originFileName = originFileName;
        this.extension = extension;
        this.multipartFile = multipartFile;
    }

    public static S3Image createImage(MultipartFile multipartFile) {
        String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename())
            .replace(BLANK, BLANK_REPLACEMENT);
        String extension = Files.getFileExtension(originalFileName);
        return new S3Image(originalFileName, extension, multipartFile);
    }

    public String generateS3FileName(Long memberId, String activeProfile) {
        String directory = activeProfile + FOLDER_DELIMITER +
            memberId + FOLDER_DELIMITER +
            LocalDate.now() + FOLDER_DELIMITER;
        return directory + UUID.randomUUID() + EXTENSION_DELIMITER + extension;
    }
}
