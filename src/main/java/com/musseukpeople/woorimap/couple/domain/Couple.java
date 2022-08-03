package com.musseukpeople.woorimap.couple.domain;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.musseukpeople.woorimap.common.model.BaseEntity;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Couple extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Embedded
    private CoupleMembers coupleMembers;

    public Couple(LocalDate startDate, CoupleMembers coupleMembers) {
        this(null, startDate, coupleMembers);
    }

    public Couple(Long id, LocalDate startDate, CoupleMembers coupleMembers) {
        checkArgument(startDate.isBefore(LocalDate.now().plusDays(1)),
            "현재 이후 날짜로 커플을 생성할 수 없습니다.");

        this.id = id;
        this.startDate = startDate;
        this.coupleMembers = coupleMembers;
        this.coupleMembers.addMembers(this);
    }

    public Couple(Long id) {
        this.id = id;
    }
}
