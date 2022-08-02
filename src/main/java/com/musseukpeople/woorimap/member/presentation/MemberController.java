package com.musseukpeople.woorimap.member.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.auth.aop.LoginRequired;
import com.musseukpeople.woorimap.auth.domain.login.Login;
import com.musseukpeople.woorimap.auth.domain.login.LoginMember;
import com.musseukpeople.woorimap.member.application.MemberService;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "멤버", description = "멤버 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 회원 가입", description = "멤버 회원 가입 API입니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.createMember(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "멤버 회원 탈퇴", description = "멤버 회원 탈퇴 API입니다.")
    @LoginRequired
    @DeleteMapping
    public ResponseEntity<Void> withdrawal(@Login LoginMember loginMember) {
        memberService.removeMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
