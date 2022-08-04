package com.musseukpeople.woorimap.post.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.post.exception.NotFoundPostException;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.post.domain.PostTag;
import com.musseukpeople.woorimap.tag.domain.Tag;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(Couple couple, CreatePostRequest createPostRequest, List<Tag> tagsOfPost) {
        Post post = createPostRequest.toPost(couple, toPostTag(tagsOfPost));
        return postRepository.save(post).getId();
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_COUPLE, postId));
    }

    private List<PostTag> toPostTag(List<Tag> tagsOfPost) {
        return tagsOfPost.stream().map(PostTag::new).collect(Collectors.toList());
    }
}
