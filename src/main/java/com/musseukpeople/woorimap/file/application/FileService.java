package com.musseukpeople.woorimap.file.application;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    List<String> uploadImages(Long id, List<MultipartFile> files);

    String uploadImage(Long id, MultipartFile file) throws IOException;
}
