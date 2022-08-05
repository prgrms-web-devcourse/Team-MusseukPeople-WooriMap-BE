package com.musseukpeople.woorimap.image.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.image.application.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "이미지", description = "이미지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class ImageController {

    private final ImageService imageService;

    @LoginRequired
    @Operation(summary = "단일 이미지 업로드", description = "이미지를 S3에 업로드해 url을 반환합니다.")
    @PostMapping("/image")
    public ResponseEntity<ApiResponse<String>> uploadImage(
        @Login LoginMember loginMember,
        @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(new ApiResponse<>(imageService.uploadImage(loginMember.getId(), file)));
    }

    @LoginRequired
    @Operation(summary = "다중 이미지 업로드", description = "이미지 리스트를 S3에 업로드해 url을 반환합니다.")
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<List<String>>> uploadImages(
        @Login LoginMember loginMember,
        @RequestPart("files") List<MultipartFile> files
    ) {
        return ResponseEntity.ok(new ApiResponse<>(imageService.uploadImages(loginMember.getId(), files)));
    }

}
