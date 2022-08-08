package com.musseukpeople.woorimap.post.domain.image;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImages {

    public static final int THUMNAIL_INDEX = 0;
    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    public PostImages(List<PostImage> postImages) {
        this.postImages = postImages;
    }

    public String getThumbnailUrl() {
        return postImages.get(THUMNAIL_INDEX).getImageUrl();
    }

    public List<String> getImageUrls() {
        return postImages.stream()
            .map(PostImage::getImageUrl)
            .collect(toList());
    }

    public void changePostImages(List<PostImage> postImagesToAdd) {
        postImages.clear();
        postImages.addAll(postImagesToAdd);
    }
}
