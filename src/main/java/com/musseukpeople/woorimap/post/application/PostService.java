package com.musseukpeople.woorimap.post.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.tag.entity.Tag;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.application.dto.CreatePostRequest;
import com.musseukpeople.woorimap.post.entity.Post;
import com.musseukpeople.woorimap.post.entity.PostRepository;
import com.musseukpeople.woorimap.post.entity.PostTag;
import com.musseukpeople.woorimap.post.entity.PostTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostTagRepository postTagRepository;

    @Transactional
    public Long createPost(Couple couple, CreatePostRequest createPostRequest) {
        Post post = createPostRequest.toPost(couple);
        return postRepository.save(post).getId();
    }

    @Transactional
    public void createPostTag(Long savePostId, List<Long> saveTagIdList) {
        List<PostTag> postTags = toPostTag(savePostId, saveTagIdList);
        postTagRepository.saveAll(postTags);
    }

    public List<PostTag> toPostTag(Long savePostId, List<Long> saveTagIdList) {
        List<PostTag> postTagList = new ArrayList<>();
        for (Long tagId : saveTagIdList) {
            postTagList.add(new PostTag(new Post(savePostId), new Tag(tagId)));
        }
        return postTagList;
    }
}
