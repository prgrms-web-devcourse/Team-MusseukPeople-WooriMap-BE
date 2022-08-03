package com.musseukpeople.woorimap.post.presentation;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.post.application.PostFacade;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글", description = "게시글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "게시글 생성 API입니다.")
    @OnlyCouple
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPost(@Valid @RequestBody CreatePostRequest createPostRequest,
                                                          @Login LoginMember member) {
        Long coupleId = member.getCoupleId();
        Long savePostId = postFacade.createPost(coupleId, createPostRequest);
        List<Long> postTagIdList = postFacade.createTag(coupleId, createPostRequest.getTags());
        postFacade.createPostTag(savePostId, postTagIdList);
        return ResponseEntity.created(URI.create("/post/" + savePostId)).build();
    }
}
