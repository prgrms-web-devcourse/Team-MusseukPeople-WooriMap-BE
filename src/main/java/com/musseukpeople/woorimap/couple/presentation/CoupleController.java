package com.musseukpeople.woorimap.couple.presentation;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.aop.OnlySolo;
import com.musseukpeople.woorimap.auth.application.dto.TokenDto;
import com.musseukpeople.woorimap.auth.application.dto.response.AccessTokenResponse;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.couple.application.CoupleFacade;
import com.musseukpeople.woorimap.couple.application.dto.request.CreateCoupleRequest;
import com.musseukpeople.woorimap.couple.application.dto.request.EditCoupleRequest;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleCheckResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleEditResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.CoupleResponse;
import com.musseukpeople.woorimap.couple.application.dto.response.InviteCodeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커플", description = "커플 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/couples")
public class CoupleController {

    private final CoupleFacade coupleFacade;

    @Operation(summary = "커플 맺기", description = "커플 맺기 API입니다.")
    @OnlySolo
    @PostMapping
    public ResponseEntity<ApiResponse<AccessTokenResponse>> createCouple(
        @Valid @RequestBody CreateCoupleRequest createCoupleRequest,
        @Login LoginMember receiver
    ) {
        TokenDto tokenDto = coupleFacade.createCouple(createCoupleRequest.getCode(), receiver);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(tokenDto.getValue());

        return ResponseEntity.ok(new ApiResponse<>(accessTokenResponse));
    }

    @Operation(summary = "커플 조회", description = "커플 조회 API입니다.")
    @OnlyCouple
    @GetMapping
    public ResponseEntity<ApiResponse<CoupleResponse>> getCouple(@Login LoginMember member) {
        CoupleResponse coupleResponse = coupleFacade.getCouple(member);
        return ResponseEntity.ok(new ApiResponse<>(coupleResponse));
    }

    @Operation(summary = "커플 수정", description = "커플 수정 API입니다.")
    @OnlyCouple
    @PutMapping
    public ResponseEntity<ApiResponse<CoupleEditResponse>> editCouple(
        @Valid @RequestBody EditCoupleRequest editCoupleRequest,
        @Login LoginMember member) {
        CoupleEditResponse coupleEditResponse = coupleFacade.modifyCouple(member.getCoupleId(),
            editCoupleRequest.getEditDate());
        return ResponseEntity.ok(new ApiResponse<>(coupleEditResponse));
    }

    @Operation(summary = "커플 해제", description = "커플 해제 API입니다.")
    @OnlyCouple
    @DeleteMapping
    public ResponseEntity<ApiResponse<AccessTokenResponse>> deleteCouple(@Login LoginMember member) {
        TokenDto tokenDto = coupleFacade.removeCouple(member);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse(tokenDto.getValue());

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(new ApiResponse<>(accessTokenResponse));
    }

    @Operation(summary = "커플 확인", description = "커플 확인 API입니다.")
    @LoginRequired
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<CoupleCheckResponse>> checkCouple(@Login LoginMember member) {
        CoupleCheckResponse coupleCheckResponse = coupleFacade.checkCouple(member.getId());
        return ResponseEntity.ok(new ApiResponse<>(coupleCheckResponse));
    }

    @Operation(summary = "커플 초대 코드 생성", description = "커플 초대 코드 생성 API입니다.")
    @OnlySolo
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<InviteCodeResponse>> createInviteCode(@Login LoginMember member) {
        LocalDateTime expireDate = LocalDateTime.now().plusDays(1);
        InviteCodeResponse inviteCodeResponse = coupleFacade.createInviteCode(member.getId(), expireDate);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ApiResponse<>(inviteCodeResponse));
    }
}
