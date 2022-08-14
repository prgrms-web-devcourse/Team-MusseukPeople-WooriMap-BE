package com.musseukpeople.woorimap.util.fixture;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.post.domain.Post;
import com.musseukpeople.woorimap.post.domain.vo.Location;
import com.musseukpeople.woorimap.tag.domain.Tag;

public class TPostBuilder {

    private Couple couple = new TCoupleBuilder().build();
    private String title = "첫 이야기";
    private String content = "<h1>첫 이야기.... </h1>";
    private List<String> imageUrls = List.of("image1", "image2");
    private List<Tag> tags = List.of(new Tag("서울", "#FFFFFF", couple), new Tag("맛집", "#FFFFFF", couple));
    private LocalDate datingDate = LocalDate.now();
    private Location location = new Location(new BigDecimal("12.12312321"), new BigDecimal("12.12312321"));

    public TPostBuilder couple(Couple couple) {
        this.couple = couple;
        this.tags = this.tags.stream()
            .map(tag -> new Tag(tag.getName(), tag.getColor(), couple))
            .collect(toList());
        return this;
    }

    public TPostBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TPostBuilder content(String content) {
        this.content = content;
        return this;
    }

    public TPostBuilder imageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    public TPostBuilder tags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public TPostBuilder datingDate(LocalDate datingDate) {
        this.datingDate = datingDate;
        return this;
    }

    public TPostBuilder location(Location location) {
        this.location = location;
        return this;
    }

    public Post build() {
        return Post.builder()
            .couple(couple)
            .title(title)
            .content(content)
            .imageUrls(imageUrls)
            .tags(tags)
            .datingDate(datingDate)
            .location(location)
            .build();
    }
}
