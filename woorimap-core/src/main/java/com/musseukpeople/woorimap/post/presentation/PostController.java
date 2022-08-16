package com.musseukpeople.woorimap.post.presentation;

import static com.musseukpeople.woorimap.common.util.LocationUriUtil.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.post.application.PostFacade;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.PostFilterCondition;
import com.musseukpeople.woorimap.post.application.dto.response.PostResponse;
import com.musseukpeople.woorimap.post.application.dto.response.PostSearchResponse;

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
                                                        @Login LoginMember loginMember) {
        Long postId = postFacade.createPost(loginMember, createPostRequest);
        return ResponseEntity.created(createLocationUri(postId)).build();
    }

    @Operation(summary = "게시글 다건 조회", description = "게시물 다건 조회 및 검색 API입니다.")
    @OnlyCouple
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostSearchResponse>>> getPosts(PostFilterCondition postFilterCondition,
                                                                          @Login LoginMember member) {
        Long coupleId = member.getCoupleId();
        List<PostSearchResponse> postSearchResponses = postFacade.searchPosts(postFilterCondition, coupleId);
        return ResponseEntity.ok(new ApiResponse<>(postSearchResponses));
    }

    @Operation(summary = "게시물 단건 조회", description = "게시물 단건 조회 API입니다.")
    @OnlyCouple
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> showPost(@Login LoginMember loginMember,
                                                              @PathVariable("postId") Long postId) {
        PostResponse postResponse = postFacade.getPost(loginMember.getCoupleId(), postId);
        return ResponseEntity.ok(new ApiResponse<>(postResponse));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API입니다.")
    @OnlyCouple
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> modifyPost(@Valid @RequestBody EditPostRequest editPostRequest,
                                                        @Login LoginMember loginMember,
                                                        @PathVariable("postId") Long postId) {
        postFacade.modifyPost(loginMember, postId, editPostRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API입니다.")
    @OnlyCouple
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@Login LoginMember loginMember,
                                                        @PathVariable("postId") Long postId) {
        postFacade.removePost(loginMember.getCoupleId(), postId);
        return ResponseEntity.noContent().build();
    }
}
