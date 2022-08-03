package com.musseukpeople.woorimap.post.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.common.model.BaseEntity;
import com.musseukpeople.woorimap.couple.domain.Couple;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    private String title;

    @Lob
    private String content;

    @Column(columnDefinition = "decimal(10,8)")
    private BigDecimal latitude;

    @Column(columnDefinition = "decimal(11,8)")
    private BigDecimal longitude;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<PostImage> postImages = new ArrayList<>();

    @Builder
    public Post(Long id, Couple couple, String title, String content,
                BigDecimal latitude, BigDecimal longitude, List<PostImage> postImages) {
        this.id = id;
        this.couple = couple;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        addPostImages(postImages);
    }

    public Post(Long id) {
        this.id = id;
    }

    public void addPostImages(List<PostImage> postImages) {
        postImages.forEach(this::addPostImages);
    }

    public void addPostImages(PostImage postImage) {
        if (postImage.getPost() != this) {
            postImage.setPost(this);
        }

        this.getPostImages().add(postImage);
    }
}
