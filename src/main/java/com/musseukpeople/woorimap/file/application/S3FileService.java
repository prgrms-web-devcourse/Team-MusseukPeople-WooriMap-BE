package com.musseukpeople.woorimap.file.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.file.domain.S3File;
import com.musseukpeople.woorimap.file.exception.ConvertFileFailedException;
import com.musseukpeople.woorimap.file.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public List<String> uploadImages(Long id, List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                return uploadImage(id, file);
            } catch (IOException e) {
                log.warn("upload Image Failed: {}", file.getOriginalFilename());
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public String uploadImage(Long id, MultipartFile multipartFile) throws IOException {
        S3File s3File = S3File.createImage(multipartFile);

        File file = convertMultipartFileToFile(s3File)
            .orElseThrow(
                () -> new ConvertFileFailedException(s3File.getOriginFileName(), ErrorCode.INTERNAL_SERVER_ERROR));

        return upload(id, s3File, file);
    }

    private String upload(Long id, S3File s3File, File file) {
        String s3ImageUrl = putS3(file, s3File.generateS3FileName(id, activeProfile));
        FileUtils.removeFile(file);
        return s3ImageUrl;
    }

    private Optional<File> convertMultipartFileToFile(S3File s3File) throws IOException {
        File file = new File(s3File.getOriginFileName());

        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(s3File.getMultipartFile().getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    private String putS3(File file, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }
}
