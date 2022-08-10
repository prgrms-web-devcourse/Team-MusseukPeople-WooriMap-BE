package com.musseukpeople.woorimap.post.application;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.application.dto.response.PostResponse;
import com.musseukpeople.woorimap.post.application.dto.response.PostSearchResponse;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.exception.PostNotBelongToCoupleException;
import com.musseukpeople.woorimap.tag.application.TagService;
import com.musseukpeople.woorimap.tag.domain.Tags;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final TagService tagService;
    private final CoupleService coupleService;

    @Transactional
    public Long createPost(Long coupleId, CreatePostRequest createPostRequest) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Tags tags = tagService.findOrCreateTags(couple, createPostRequest.getTags());

        Post post = postService.createPost(couple, tags.getList(), createPostRequest);
        return post.getId();
    }

    public List<PostSearchResponse> searchPosts(PostFilterCondition postFilterCondition, Long coupleId) {
        return postService.searchPosts(postFilterCondition, coupleId);
    }

    @Transactional
    public Long modifyPost(Long coupleId, Long postId, EditPostRequest editPostRequest) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Tags tags = tagService.findOrCreateTags(couple, editPostRequest.getTags());

        Post post = postService.modifyPost(tags.getList(), postId, editPostRequest);
        return post.getId();
    }

    @Transactional
    public void removePost(Long coupleId, Long postId) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Post post = postService.getPostById(postId);
        if (post.isNotWrittenBy(couple)) {
            throw new PostNotBelongToCoupleException(coupleId, postId, ErrorCode.NOT_BELONG_TO_COUPLE);
        }
        postService.removePost(postId);
    }

    public PostResponse getPost(Long coupleId, Long postId) {
        Couple couple = coupleService.getCoupleById(coupleId);
        Post post = postService.getPostWithFetchById(postId);
        if (post.isNotWrittenBy(couple)) {
            throw new PostNotBelongToCoupleException(coupleId, postId, ErrorCode.NOT_BELONG_TO_COUPLE);
        }
        return PostResponse.from(post);
    }
}
