package com.musseukpeople.woorimap.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.application.dto.response.MemberResponse;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.exception.DuplicateEmailException;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;
import com.musseukpeople.woorimap.util.IntegrationTest;
import com.musseukpeople.woorimap.util.fixture.TCoupleBuilder;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CoupleRepository coupleRepository;

    @DisplayName("회원 생성 성공")
    @Test
    void createMember_success() {
        // given
        SignupRequest signupRequest = new SignupRequest("test@gmail.com", "!@Hwan123", "우리맵");

        // when
        Long id = memberService.createMember(signupRequest);

        // then
        assertThat(id).isPositive();
    }

    @DisplayName("중복 이메일로 인한 회원 생성 실패")
    @Test
    void createMember_duplicateEmail_fail() {
        // given
        String email = "test@gmail.com";
        memberService.createMember(new SignupRequest(email, "!@Hwan123", "우리맵"));
        SignupRequest duplicateEmailRequest = new SignupRequest(email, "!@Test123", "우리맵");

        // when
        // then
        assertThatThrownBy(() -> memberService.createMember(duplicateEmailRequest))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("이메일로 회원 찾기 성공")
    @Test
    void getMemberByEmail_success() {
        // given
        String email = "test@gmail.com";
        Long memberId = memberService.createMember(new SignupRequest(email, "!@Hwan123", "우리맵"));

        // when
        Member member = memberService.getMemberByEmail(email);

        // then
        assertThat(member.getId()).isEqualTo(memberId);
    }

    @DisplayName("저장되지 않는 이메일로 인한 회원 찾기 실패")
    @Test
    void getMemberByEmail_notFoundEmail_fail() {
        // given
        memberService.createMember(new SignupRequest("test@gmail.com", "!@Hwan123", "우리맵"));

        // when
        // then
        assertThatThrownBy(() -> memberService.getMemberByEmail("tt@gmail.com"))
            .isInstanceOf(LoginFailedException.class)
            .hasMessageContaining("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("회원 정보 조회 성공 - 솔로")
    @Test
    void getMemberResponse_solo_success() {
        // given
        Member member = new TMemberBuilder().email("test@gmail.com").build();
        Long id = memberRepository.save(member).getId();

        // when
        MemberResponse memberResponse = memberService.getMemberResponseById(id);

        // then
        assertAll(
            () -> assertThat(memberResponse.getCoupleNickName()).isNull(),
            () -> assertThat(memberResponse.getCoupleStartingDate()).isNull()
        );
    }

    @DisplayName("회원 정보 조회 성공 - 커플")
    @Test
    void getMemberResponse_couple_success() {
        // given
        Couple couple = new TCoupleBuilder().build();
        coupleRepository.save(couple);
        Member member = new TMemberBuilder().email("test@gmail.com").couple(couple).build();
        Member opponentMember = new TMemberBuilder().email("oppent@gmail.com").couple(couple).build();
        Long id = memberRepository.save(member).getId();
        memberRepository.save(opponentMember);

        // when
        MemberResponse memberResponse = memberService.getMemberResponseById(id);

        // then
        assertAll(
            () -> assertThat(memberResponse.getCoupleNickName()).isEqualTo(opponentMember.getNickName().getValue()),
            () -> assertThat(memberResponse.getCoupleStartingDate()).isEqualTo(LocalDate.now())
        );
    }
}
