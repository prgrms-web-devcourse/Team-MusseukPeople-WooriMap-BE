package com.musseukpeople.woorimap.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.domain.Member;
import com.musseukpeople.woorimap.member.domain.MemberRepository;
import com.musseukpeople.woorimap.member.domain.vo.Email;
import com.musseukpeople.woorimap.member.domain.vo.NickName;
import com.musseukpeople.woorimap.member.domain.vo.Password;
import com.musseukpeople.woorimap.member.exception.DuplicateEmailException;
import com.musseukpeople.woorimap.member.exception.LoginFailedException;
import com.musseukpeople.woorimap.member.exception.NotFoundMemberException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createMember(SignupRequest signupRequest) {
        String email = signupRequest.getEmail();
        validateDuplicateEmail(email);

        Member member = Member.builder()
            .email(new Email(email))
            .password(Password.encryptPassword(passwordEncoder, signupRequest.getPassword()))
            .nickName(new NickName(signupRequest.getNickName()))
            .build();
        return memberRepository.save(member).getId();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER, id));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
            .orElseThrow(() -> new LoginFailedException(ErrorCode.LOGIN_FAILED));
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmailValue(email)) {
            throw new DuplicateEmailException(email, ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}
