package com.musseukpeople.woorimap.tag.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.musseukpeople.woorimap.couple.domain.Couple;

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

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Couple couple;

    private String color;

    public Tag(String name, String color, Couple couple) {
        this.name = name;
        this.color = color;
        this.couple = couple;
    }

    public Tag(Long id) {
        this.id = id;
    }
}
