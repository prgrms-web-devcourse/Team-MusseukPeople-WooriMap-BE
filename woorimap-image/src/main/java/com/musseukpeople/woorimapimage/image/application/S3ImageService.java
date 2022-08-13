package com.musseukpeople.woorimapimage.image.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.musseukpeople.woorimapimage.common.exception.ErrorCode;
import com.musseukpeople.woorimapimage.common.util.FileUtil;
import com.musseukpeople.woorimapimage.image.domain.S3Image;
import com.musseukpeople.woorimapimage.image.exception.ConvertFileFailedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3ImageService implements ImageService {

    private final AmazonS3Client amazonS3Client;

    private final String bucketName;

    private final String activeProfile;

    private final Executor executor;

    public S3ImageService(
        AmazonS3Client amazonS3Client,
        @Value("${cloud.aws.s3.bucket}") String bucketName,
        @Value("${spring.profiles.active}") String activeProfile,
        @Qualifier("imageExecutor") Executor executor) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
        this.activeProfile = activeProfile;
        this.executor = executor;
    }

    @Override
    public List<String> uploadImages(Long id, List<MultipartFile> files) {
        List<CompletableFuture<String>> imageUrlFutures = files.stream()
            .map(file -> CompletableFuture.supplyAsync(() -> uploadImage(id, file), executor)
                .orTimeout(3, TimeUnit.SECONDS))
            .collect(Collectors.toList());

        return imageUrlFutures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    @Override
    public String uploadImage(Long id, MultipartFile multipartFile) {
        S3Image s3Image = S3Image.createImage(multipartFile);

        File file = convertMultipartFileToFile(s3Image)
            .orElseThrow(
                () -> new ConvertFileFailedException(s3Image.getOriginFileName(), ErrorCode.INTERNAL_SERVER_ERROR));

        return upload(id, s3Image, file);
    }

    private String upload(Long id, S3Image s3Image, File file) {
        String s3ImageUrl = putS3(file, s3Image.generateS3FileName(id, activeProfile));
        FileUtil.removeFile(file);
        return s3ImageUrl;
    }

    private Optional<File> convertMultipartFileToFile(S3Image s3Image) {
        File file = new File(s3Image.getOriginFileName());

        try {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(s3Image.getMultipartFile().getBytes());
                }
                return Optional.of(file);
            }
            return Optional.empty();
        } catch (IOException e) {
            log.error("convert multipartfile to file failed: {}", s3Image.getOriginFileName(), e);
            return Optional.empty();
        }
    }

    private String putS3(File file, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
}
