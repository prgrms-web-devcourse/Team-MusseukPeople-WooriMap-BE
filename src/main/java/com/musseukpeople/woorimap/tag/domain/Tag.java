package com.musseukpeople.woorimap.tag.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.tag.domain.vo.TagColor;
import com.musseukpeople.woorimap.tag.domain.vo.TagName;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TagName name;

    @Embedded
    private TagColor color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "couple_id")
    private Couple couple;

    public Tag(String name, String color, Couple couple) {
        this(new TagName(name), new TagColor(color), couple);
    }

    public Tag(TagName name, TagColor color, Couple couple) {
        this.name = name;
        this.color = color;
        this.couple = couple;
    }

    public boolean isSameName(Tag tag) {
        return this.name.equals(tag.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getColor() {
        return this.color.getValue();
    }

    public Couple getCouple() {
        return couple;
    }
}
