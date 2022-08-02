package com.musseukpeople.woorimap.couple.presentation;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.OnlyCouple;
import com.musseukpeople.woorimap.auth.aop.OnlySolo;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.couple.application.CoupleFacade;
import com.musseukpeople.woorimap.couple.application.dto.request.CreateCoupleRequest;
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
    @PostMapping
    public ResponseEntity<Void> createCouple(
        @Valid @RequestBody CreateCoupleRequest createCoupleRequest,
        @Login LoginMember receiver
    ) {
        coupleFacade.createCouple(createCoupleRequest.getCode(), receiver);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "커플 해제", description = "커플 해제 API입니다.")
    @OnlyCouple
    @DeleteMapping
    public ResponseEntity<Void> deleteCouple(@Login LoginMember member) {
        coupleFacade.removeCouple(member);
        return ResponseEntity.noContent().build();
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
