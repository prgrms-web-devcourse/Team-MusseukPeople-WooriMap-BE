package com.musseukpeople.woorimap.post.domain;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.musseukpeople.woorimap.common.model.BaseEntity;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.domain.image.PostImage;
import com.musseukpeople.woorimap.post.domain.image.PostImages;
import com.musseukpeople.woorimap.post.domain.tag.PostTag;
import com.musseukpeople.woorimap.post.domain.tag.PostTags;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.domain.Tag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Embedded
    private Location location;

    @Column(nullable = false)
    private LocalDate datingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    @Embedded
    private PostImages postImages;

    @Embedded
    private PostTags postTags;

    @Builder
    public Post(Couple couple, String title, String content, Location location, LocalDate datingDate,
                List<String> imageUrls, List<Tag> tags) {
        this.couple = couple;
        this.title = title;
        this.content = content;
        this.location = location;
        this.datingDate = datingDate;
        this.postImages = new PostImages(convertToPostImages(imageUrls));
        this.postTags = new PostTags(covertToPostTags(tags));
    }

    private List<PostTag> covertToPostTags(List<Tag> tags) {
        return tags.stream()
            .map(tag -> new PostTag(this, tag))
            .collect(toList());
    }

    private List<PostImage> convertToPostImages(List<String> imageUrls) {
        return imageUrls.stream()
            .map(imageUrl -> new PostImage(this, imageUrl))
            .collect(toList());
    }
}
