package com.musseukpeople.woorimap.post.presentation;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.post.application.PostFacade;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "게시글", description = "게시글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/couples/posts")
public class PostController {

    private final PostFacade postFacade;

    @Operation(summary = "게시글 생성", description = "게시글 생성 API입니다.")
    @OnlyCouple
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createPost(@Valid @RequestBody CreatePostRequest createPostRequest,
                                                        @Login LoginMember member) {
        Long coupleId = member.getCoupleId();
        Long postId = postFacade.createPost(coupleId, createPostRequest);
        return ResponseEntity.created(createURI(postId)).build();
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API입니다.")
    @OnlyCouple
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> modifyPost(@Valid @RequestBody EditPostRequest editPostRequest,
                                                        @Login LoginMember member,
                                                        @PathVariable("postId") Long postId) {
        Long coupleId = member.getCoupleId();
        postFacade.modifyPost(coupleId, postId, editPostRequest);
        return ResponseEntity.ok().build();
    }

    private URI createURI(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }
}
