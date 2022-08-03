package com.musseukpeople.woorimap.tag.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.entity.Tag;
import com.musseukpeople.woorimap.common.model.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tag>>> getTags() {
        List<Tag> tags = tagService.getTags();
        return ResponseEntity.ok(new ApiResponse<>(tags));
    }
}
