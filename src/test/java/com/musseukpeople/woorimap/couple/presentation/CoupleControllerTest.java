package com.musseukpeople.woorimap.couple.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.couple.domain.Couple;
import com.musseukpeople.woorimap.couple.domain.CoupleRepository;
import com.musseukpeople.woorimap.couple.domain.InviteCode;
import com.musseukpeople.woorimap.couple.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.util.AcceptanceTest;

class CoupleControllerTest extends AcceptanceTest {

    private static final String email = "test@gmail.com";
    private static final String password = "!Test1234";
    private static final String nickName = "test";

    @Autowired
    private CoupleRepository coupleRepository;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private String accessToken;

    @BeforeEach
    void login() throws Exception {
        회원가입(new SignupRequest(email, password, nickName));
        
        makeCoupleAndAddMember();

        accessToken = 로그인_토큰(new SignInRequest(email, password));
    }

    @DisplayName("커플 초대 코드 생성 API 성공")
    @Test
    void inviteCodeCreate_success() throws Exception {
        //given
        MockHttpServletResponse response = createInviteCodeApi();

        //when
        InviteCodeResponse code = getResponseObject(response, InviteCodeResponse.class);

        //then
        assertThat(code.getCode()).hasSize(7);
    }

    @DisplayName("두번째 코드 생성시 만료기간이 지나지 않았다면 같은 코드 조회")
    @Test
    void inviteCodeGet_notOverExpireDate_success() throws Exception {
        //given
        MockHttpServletResponse firstResponse = createInviteCodeApi();
        MockHttpServletResponse secondResponse = createInviteCodeApi();

        //when
        InviteCodeResponse code1 = getResponseObject(firstResponse, InviteCodeResponse.class);
        InviteCodeResponse code2 = getResponseObject(secondResponse, InviteCodeResponse.class);

        //then
        assertThat(code1.getCode()).isEqualTo(code2.getCode());
    }

    @DisplayName("만료기한이 넘으면 새로운 코드 생성")
    @Test
    void inviteCodeCreate_overExpireDate_success() throws Exception {
        //given
        InviteCode savedCode = inviteCodeRepository.findById(
            getResponseObject(createInviteCodeApi(), InviteCodeResponse.class).getCode()).get();
        inviteCodeRepository.save(
            new InviteCode(savedCode.getCode(), savedCode.getInviterId(), LocalDateTime.now().minusDays(10)));

        String savedInviteCode = savedCode.getCode();

        //when
        MockHttpServletResponse newResponse = createInviteCodeApi();
        InviteCodeResponse newInviteCode = getResponseObject(newResponse, InviteCodeResponse.class);

        //then
        assertThat(newInviteCode.getCode()).isNotEqualTo(savedInviteCode);
    }

    @DisplayName("커플 해제 성공")
    @Test
    void removeCouple_success() throws Exception {
        //given
        //when
        mockMvc.perform(delete("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, accessToken))

            //then
            .andExpect(status().isNoContent())
            .andDo(print());

        Optional<Member> foundMember = memberRepository.findMemberByEmail(email);

        Assertions.assertAll(
            () -> assertThat(foundMember).isPresent(),
            () -> assertThat(foundMember.get().isCouple()).isFalse()
        );
    }

    private MockHttpServletResponse createInviteCodeApi() throws Exception {
        return mockMvc.perform(post("/api/couples/invite")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn().getResponse();
    }

    private void makeCoupleAndAddMember() {
        Couple couple = new Couple(LocalDate.of(2022, 1, 1));
        Member member = memberRepository.findMemberByEmail(email).get();
        couple.addMember(member);
        coupleRepository.save(couple);
        memberRepository.save(member);
    }
}
