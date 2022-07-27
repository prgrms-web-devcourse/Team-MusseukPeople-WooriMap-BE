package com.musseukpeople.woorimap.member.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;

@RestController
@RequestMapping("api/members")
public class MemberController {

	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest signupRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
