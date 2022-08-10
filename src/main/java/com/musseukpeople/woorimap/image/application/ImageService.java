package com.musseukpeople.woorimap.image.application;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    List<String> uploadImages(Long id, List<MultipartFile> files);

    String uploadImage(Long id, MultipartFile file);
}
