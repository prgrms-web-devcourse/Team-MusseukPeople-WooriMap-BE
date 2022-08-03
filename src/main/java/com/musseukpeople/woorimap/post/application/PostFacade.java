package com.musseukpeople.woorimap.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final TagService tagService;
    private final CoupleService coupleService;

    @Transactional
    public Long createPost(Long coupleId, CreatePostRequest createPostRequest) {
        Couple couple = coupleService.getCoupleById(coupleId);
        List<Long> postTagIdList = tagService.createTag(couple, createPostRequest.getTags());
        return postService.createPost(couple, createPostRequest, postTagIdList);
    }
}
