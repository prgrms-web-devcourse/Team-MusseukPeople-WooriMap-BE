package com.musseukpeople.woorimap.post.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.post.exception.NotFoundPostException;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.entity.Post;
import com.musseukpeople.woorimap.post.entity.PostRepository;
import com.musseukpeople.woorimap.post.entity.PostTag;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(Couple couple, CreatePostRequest createPostRequest, List<Long> postTagIdList) {
        Post post = createPostRequest.toPost(couple, toPostTag(postTagIdList));
        return postRepository.save(post).getId();
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_COUPLE, postId));
    }

    public List<PostTag> toPostTag(List<Long> saveTagIdList) {
        List<PostTag> postTagList = new ArrayList<>();
        for (Long tagId : saveTagIdList) {
            postTagList.add(new PostTag(tagId));
        }
        return postTagList;
    }
}
