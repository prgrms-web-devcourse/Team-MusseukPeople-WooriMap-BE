package com.musseukpeople.woorimap.couple.domain;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.musseukpeople.woorimap.common.model.BaseEntity;
import com.musseukpeople.woorimap.member.domain.Member;

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

    @OneToMany(mappedBy = "couple")
    private List<Member> members = new ArrayList<>();

    public Couple(LocalDate startDate) {
        this(null, startDate);
    }

    public Couple(Long id, LocalDate startDate) {
        checkArgument(startDate.isBefore(LocalDate.now().plusDays(1)),
            "현재 이후 날짜로 커플을 생성할 수 없습니다.");

        this.id = id;
        this.startDate = startDate;
    }

    public void addMember(Member member) {
        this.members.add(member);

        member.changeCouple(this);
    }
}
