package com.musseukpeople.woorimap.tag.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.application.dto.response.TagResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "태그", description = "태그 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/couples/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 전체 조회", description = "태그 전체 조회 API입니다.")
    @OnlyCouple
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponse>>> showAll(@Login LoginMember loginMember) {
        List<TagResponse> tagResponses = tagService.getCoupleTags(loginMember.getCoupleId());
        return ResponseEntity.ok(new ApiResponse<>(tagResponses));
    }
}
