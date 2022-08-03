package com.musseukpeople.woorimap.post.application;

import java.util.List;

import org.springframework.stereotype.Component;

import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.application.dto.TagRequest;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostFacade {
    private final PostService postService;
    private final TagService tagService;

    public Long createPost(Long coupleId, CreatePostRequest createPostRequest) {
        return postService.createPost(coupleId, createPostRequest);
    }

    public List<Long> createTag(Long coupleId, List<TagRequest> tags) {
        return tagService.createTag(coupleId, tags);
    }

    public void createPostTag(Long savePostId, List<Long> postTagIdList) {
        postService.createPostTag(savePostId, postTagIdList);
    }
}
