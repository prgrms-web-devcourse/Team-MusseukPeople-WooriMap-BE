package com.musseukpeople.woorimap.couple.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.AuthService;
import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.auth.application.dto.response.TokenResponse;
import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;
import com.musseukpeople.woorimap.invitecode.domain.InviteCode;
import com.musseukpeople.woorimap.invitecode.domain.InviteCodeRepository;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.util.AcceptanceTest;
import com.musseukpeople.woorimap.util.fixture.TMemberBuilder;

class CoupleControllerTest extends AcceptanceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    private String accessToken;

    @BeforeEach
    void login() throws Exception {
        Member member = new TMemberBuilder().build();
        회원가입(new SignupRequest(member.getEmail().getValue(), "Aaaaa11!!",
            member.getNickName().getValue()));
        TokenResponse tokenResponse = authService.login(new SignInRequest(member.getEmail().getValue(), "Aaaaa11!!"));
        accessToken = "Bearer " + tokenResponse.getAccessToken();
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

    private MockHttpServletResponse createInviteCodeApi() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/couples/invite")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andDo(print())
            .andReturn().getResponse();
        return response;
    }
}
