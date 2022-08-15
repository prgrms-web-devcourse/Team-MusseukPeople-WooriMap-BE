package com.musseukpeople.woorimap.auth.application.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequest {

    @Schema(description = "이메일")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    private String email;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    private String password;

    public SignInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
