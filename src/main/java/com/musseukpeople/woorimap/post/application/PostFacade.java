package com.musseukpeople.woorimap.post.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.PostRequest;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.domain.Tags;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final TagService tagService;
    private final CoupleService coupleService;

    @Transactional
    public Long createPost(Long coupleId, PostRequest createPostRequest) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Tags tags = tagService.findOrCreateTags(couple, createPostRequest.getTags());
        return postService.createPost(couple, tags.getList(), createPostRequest);
    }

    @Transactional
    public Long modifyPost(Long coupleId, Long postId, PostRequest editPostRequest) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Tags tags = tagService.findOrCreateTags(couple, editPostRequest.getTags());
        return postService.modifyPost(tags.getList(), postId, editPostRequest);
    }
}
