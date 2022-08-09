package com.musseukpeople.woorimap.post.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.request.CreatePostRequest;
import com.musseukpeople.woorimap.post.application.dto.request.EditPostRequest;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.PostRepository;
import com.musseukpeople.woorimap.post.exception.NotFoundPostException;
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

    @Transactional
    public Long modifyPost(List<Tag> tags, Long postId, EditPostRequest editPostRequest) {
        Post post = getPostById(postId);

        post.changeTitle(editPostRequest.getTitle());
        post.changeContent(editPostRequest.getContent());
        post.changeDatingDate(editPostRequest.getDatingDate());
        post.changeLocation(editPostRequest.getLatitude(), editPostRequest.getLongitude());
        post.changePostImages(editPostRequest.getImageUrls());
        post.changePostTags(tags);

        return post.getId();
    }

    private Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new NotFoundPostException(ErrorCode.NOT_FOUND_POST, id));
    }
}
