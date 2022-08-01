package com.musseukpeople.woorimap.couple.presentation;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.common.model.ApiResponse;
import com.musseukpeople.woorimap.couple.application.CoupleFacade;
import com.musseukpeople.woorimap.invitecode.application.dto.response.InviteCodeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "커플", description = "커플 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/couples")
public class CoupleController {

    private final CoupleFacade coupleFacade;

    @Operation(summary = "커플 초대 코드 생성", description = "커플 초대 코드 생성 API입니다.")
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<InviteCodeResponse>> createInviteCode(@Login LoginMember member) {
        LocalDateTime expireDate = LocalDateTime.now().plusDays(1);
        InviteCodeResponse inviteCodeResponse = coupleFacade.createInviteCode(member.getId(), expireDate);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ApiResponse<>(inviteCodeResponse));
    }
}