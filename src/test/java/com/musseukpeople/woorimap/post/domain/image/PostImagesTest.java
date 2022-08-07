package com.musseukpeople.woorimap.post.domain.image;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostImagesTest {

    @DisplayName("썸네일 반환 성공")
    @Test
    void getThumbnailUrl() {
        // given
        String thumbnailImageUrl = "Thumbnail";
        PostImage thumbnail = new PostImage(null, thumbnailImageUrl);
        PostImage secondImage = new PostImage(null, "second");
        PostImages postImages = new PostImages(List.of(thumbnail, secondImage));

        // when
        String thumbnailUrl = postImages.getThumbnailUrl();

        // then
        assertThat(thumbnailUrl).isEqualTo(thumbnailImageUrl);
    }

    @DisplayName("이미지URL 반환 성공")
    @Test
    void getImageUrls() {
        // given
        PostImage firstImage = new PostImage(null, "first");
        PostImage secondImage = new PostImage(null, "second");
        PostImages postImages = new PostImages(List.of(firstImage, secondImage));

        // when
        List<String> imageUrls = postImages.getImageUrls();

        // then
        assertThat(imageUrls).hasSize(2);
    }
}
