package com.musseukpeople.woorimapimage.image.presentation;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.musseukpeople.woorimapimage.common.model.ApiResponse;
import com.musseukpeople.woorimapimage.image.application.ImageService;
import com.musseukpeople.woorimapimage.image.presentation.auth.login.Login;
import com.musseukpeople.woorimapimage.image.presentation.auth.login.LoginMember;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "이미지", description = "이미지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
@SecurityRequirement(name = "bearer")
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "단일 이미지 업로드", description = "이미지를 S3에 업로드해 url을 반환합니다.")
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadImage(@Login LoginMember loginMember,
                                                           @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(new ApiResponse<>(imageService.uploadImage(loginMember.getId(), file)));
    }

    @Operation(summary = "다중 이미지 업로드", description = "이미지 리스트를 S3에 업로드해 url을 반환합니다.")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(@Login LoginMember loginMember,
                                                                  @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(new ApiResponse<>(imageService.uploadImages(loginMember.getId(), files)));
    }

}
