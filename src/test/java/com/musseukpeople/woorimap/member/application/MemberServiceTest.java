package com.musseukpeople.woorimap.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.exception.DuplicateEmailException;
import com.musseukpeople.woorimap.member.exception.NotFoundMemberException;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

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
    void createMember_fail_duplicateEmail() {
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
    void getMemberByEmail() {
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
    void getMemberByEmail_notFoundEmail() {
        // given
        memberService.createMember(new SignupRequest("test@gmail.com", "!@Hwan123", "우리맵"));

        // when
        // then
        assertThatThrownBy(() -> memberService.getMemberByEmail("tt@gmail.com"))
            .isInstanceOf(NotFoundMemberException.class)
            .hasMessageContaining("존재하지 않는 회원 이메일입니다.");
    }
}
