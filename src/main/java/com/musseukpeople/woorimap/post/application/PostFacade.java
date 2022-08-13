package com.musseukpeople.woorimap.post.application;

import static com.musseukpeople.woorimap.event.domain.PostEvent.EventType.*;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.application.CoupleService;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.event.domain.PostEvent;
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
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createPost(LoginMember loginMember, CreatePostRequest createPostRequest) {
        Couple couple = coupleService.getCoupleWithMemberById(loginMember.getCoupleId());
        Tags tags = tagService.findOrCreateTags(couple, createPostRequest.getTags());

        Post post = postService.createPost(couple, tags.getList(), createPostRequest);
        eventPublisher.publishEvent(PostEvent.of(loginMember.getId(), post, POST_CREATED));
        return post.getId();
    }

    @Transactional
    public Long modifyPost(LoginMember loginMember, Long postId, EditPostRequest editPostRequest) {
        Couple couple = coupleService.getCoupleWithMemberById(loginMember.getCoupleId());
        Tags tags = tagService.findOrCreateTags(couple, editPostRequest.getTags());

        Post post = postService.modifyPost(tags.getList(), postId, editPostRequest);
        eventPublisher.publishEvent(PostEvent.of(loginMember.getId(), post, POST_CREATED));
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

    public List<PostSearchResponse> searchPosts(PostFilterCondition postFilterCondition, Long coupleId) {
        return postService.searchPosts(postFilterCondition, coupleId);
    }
}
