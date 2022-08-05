package com.musseukpeople.woorimap.couple.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import com.musseukpeople.woorimap.auth.application.dto.request.SignInRequest;
import com.musseukpeople.woorimap.couple.application.dto.request.CreateCoupleRequest;
import com.musseukpeople.woorimap.couple.application.dto.request.EditCoupleRequest;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleCheckResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleEditResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;
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
        accessToken = 회원가입_토큰(new SignupRequest(email, password, nickName));
    }

    @DisplayName("커플 맺기 API 성공")
    @Test
    void createCouple_success() throws Exception {
        //given
        String rEmail = "receiver@gmail.com";
        String rPassword = "!Recevier123";
        String rNickName = "receiver";
        String inviteCode = createInviteCodeApi(accessToken).getContentAsString().replaceAll("[^0-9]", "");
        회원가입(new SignupRequest(rEmail, rPassword, rNickName));
        String receiverToken = 로그인_토큰(new SignInRequest(rEmail, rPassword));
        CreateCoupleRequest createCoupleRequest = new CreateCoupleRequest(inviteCode);

        //when
        mockMvc.perform(post("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, receiverToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCoupleRequest)))

            //then
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("자기 자신은 커플로 맺을 수 없다.")
    @Test
    void createCouple_selfCreate_fail() throws Exception {
        //given
        String inviteCode = createInviteCodeApi(accessToken).getContentAsString().replaceAll("[^0-9]", "");
        CreateCoupleRequest createCoupleRequest = new CreateCoupleRequest(inviteCode);

        //when
        mockMvc.perform(post("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCoupleRequest)))
            //then
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @DisplayName("커플 조회 성공")
    @Test
    void getCouple_success() throws Exception {
        //given
        String coupleToken = 커플_맺기_토큰(accessToken);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, coupleToken))

            //then
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse();

        CoupleResponse coupleResponse = getResponseObject(response, CoupleResponse.class);

        assertAll(
            () -> assertThat(coupleResponse.getMe().getNickName()).isEqualTo(nickName),
            () -> assertThat(coupleResponse.getYou().getNickName()).isEqualTo("inviter")
        );
    }

    @DisplayName("커플이 아니면 커플 조회 실패")
    @Test
    void getCouple_notCouple_fail() throws Exception {
        //given
        //when
        mockMvc.perform(get("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, accessToken))

            //then
            .andExpect(status().isForbidden())
            .andDo(print());
    }

    @DisplayName("커플 수정 API 성공")
    @Test
    void modify_success() throws Exception {
        //given
        String coupleToken = 커플_맺기_토큰(accessToken);
        LocalDate modifyDate = LocalDate.of(1996, 6, 28);
        EditCoupleRequest editCoupleRequest = new EditCoupleRequest(modifyDate);

        //when
        MockHttpServletResponse response = mockMvc.perform(put("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, coupleToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editCoupleRequest)))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse();

        CoupleEditResponse editResponse = getResponseObject(response, CoupleEditResponse.class);

        //then
        assertThat(editResponse.getStartDate()).isEqualTo(modifyDate);
    }

    @DisplayName("커플 초대 코드 생성 API 성공")
    @Test
    void inviteCodeCreate_success() throws Exception {
        //given
        MockHttpServletResponse response = createInviteCodeApi(accessToken);

        //when
        InviteCodeResponse code = getResponseObject(response, InviteCodeResponse.class);

        //then
        assertThat(code.getCode()).hasSize(7);
    }

    @DisplayName("두번째 코드 생성시 만료기간이 지나지 않았다면 같은 코드 조회")
    @Test
    void inviteCodeGet_notOverExpireDate_success() throws Exception {
        //given
        MockHttpServletResponse firstResponse = createInviteCodeApi(accessToken);
        MockHttpServletResponse secondResponse = createInviteCodeApi(accessToken);

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
            getResponseObject(createInviteCodeApi(accessToken), InviteCodeResponse.class).getCode()).get();
        inviteCodeRepository.save(
            new InviteCode(savedCode.getCode(), savedCode.getInviterId(), LocalDateTime.now().minusDays(10)));

        String savedInviteCode = savedCode.getCode();

        //when
        MockHttpServletResponse newResponse = createInviteCodeApi(accessToken);
        InviteCodeResponse newInviteCode = getResponseObject(newResponse, InviteCodeResponse.class);

        //then
        assertThat(newInviteCode.getCode()).isNotEqualTo(savedInviteCode);
    }

    @DisplayName("커플 해제 성공")
    @Test
    void removeCouple_success() throws Exception {
        //given
        String coupleToken = 커플_맺기_토큰(accessToken);

        //when
        mockMvc.perform(delete("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, coupleToken))

            //then
            .andExpect(status().isNoContent())
            .andDo(print());

        Optional<Member> foundMember = memberRepository.findMemberByEmail(email);

        assertAll(
            () -> assertThat(foundMember).isPresent(),
            () -> assertThat(foundMember.get().isCouple()).isFalse()
        );
    }

    @DisplayName("커플이 아니면 커플 해제 실패")
    @Test
    void removeCouple_notCouple_fail() throws Exception {
        //given
        String notCoupleAccessToken = accessToken;

        //when
        mockMvc.perform(delete("/api/couples")
                .header(HttpHeaders.AUTHORIZATION, notCoupleAccessToken))

            //then
            .andExpect(status().isForbidden())
            .andDo(print());
    }

    @DisplayName("솔로라서 커플 확인 실패")
    @Test
    void check_solo_fail() throws Exception {
        //given
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/api/couples/check")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse();
        CoupleCheckResponse checkResponse = getResponseObject(response, CoupleCheckResponse.class);

        //then
        assertAll(
            () -> assertThat(checkResponse.getAccessToken()).isNull(),
            () -> assertThat(checkResponse.getIsCouple()).isFalse()
        );
    }

    @DisplayName("커플 확인 API 성공")
    @Test
    void check_couple_fail() throws Exception {
        //given
        String coupleToken = 커플_맺기_토큰(accessToken);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/api/couples/check")
                .header(HttpHeaders.AUTHORIZATION, coupleToken))

            //then
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn().getResponse();

        CoupleCheckResponse checkResponse = getResponseObject(response, CoupleCheckResponse.class);

        assertThat(checkResponse.getAccessToken()).isNotNull();
        assertThat(checkResponse.getIsCouple()).isTrue();
    }

    private MockHttpServletResponse createInviteCodeApi(String accessToken) throws Exception {
        return mockMvc.perform(post("/api/couples/invite")
                .header(HttpHeaders.AUTHORIZATION, accessToken))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn().getResponse();
    }
}
