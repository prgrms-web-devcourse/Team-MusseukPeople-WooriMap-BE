package com.musseukpeople.woorimap.util.fixture;

import java.time.LocalDate;
import java.util.List;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.vo.CoupleMembers;

public class TCoupleBuilder {

    private static final String INVITER_EMAIL = "inviter1@gmail.com";
    private static final String RECEIVER_EMAIL = "receiver@gmail.com";
    private static final TMemberBuilder memberBuilder = new TMemberBuilder();

    private Long coupleId;
    private LocalDate startDate = LocalDate.now();
    private CoupleMembers members = new CoupleMembers(
        List.of(memberBuilder.email(INVITER_EMAIL).build(), memberBuilder.email(RECEIVER_EMAIL).build()));

    public TCoupleBuilder id(Long coupleId) {
        this.coupleId = coupleId;
        return this;
    }

    public TCoupleBuilder startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public TCoupleBuilder coupleMembers(CoupleMembers coupleMembers) {
        this.members = coupleMembers;
        return this;
    }

    public Couple build() {
        return new Couple(coupleId, startDate, members);
    }
}
