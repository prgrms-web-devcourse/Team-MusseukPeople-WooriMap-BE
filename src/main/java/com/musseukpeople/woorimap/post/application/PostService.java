package com.musseukpeople.woorimap.post.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.tag.domain.Tag;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(Couple couple, List<Tag> tags, CreatePostRequest createPostRequest) {
        Post post = createPostRequest.toPost(couple, tags);
        return postRepository.save(post).getId();
    }
}
