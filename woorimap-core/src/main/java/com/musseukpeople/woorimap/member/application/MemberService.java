package com.musseukpeople.woorimap.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.musseukpeople.woorimap.common.exception.ErrorCode;
import com.musseukpeople.woorimap.member.application.dto.request.EditProfileRequest;
import com.musseukpeople.woorimap.member.application.dto.request.SignupRequest;
import com.musseukpeople.woorimap.member.application.dto.response.MemberResponse;
import com.musseukpeople.woorimap.member.application.dto.response.ProfileResponse;
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

    @Transactional
    public ProfileResponse modifyMember(Long id, EditProfileRequest editProfileRequest) {
        Member member = getMemberById(id);
        String nickName = editProfileRequest.getNickName();
        String imageUrl = editProfileRequest.getImageUrl();

        member.changeNickName(nickName);
        member.changeProfileImage(imageUrl);
        return new ProfileResponse(imageUrl, nickName);
    }

    public MemberResponse getMemberResponseById(Long id) {
        Member member = getMemberWithCoupleById(id);
        return MemberResponse.from(member);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER, id));
    }

    public Member getMemberWithCoupleById(Long id) {
        return memberRepository.findMemberWithCoupleById(id)
            .orElseThrow(() -> new NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER, id));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
            .orElseThrow(() -> new LoginFailedException(ErrorCode.LOGIN_FAILED));
    }

    @Transactional
    public void removeMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    public void breakUpMembersByCoupleId(Long coupleId) {
        memberRepository.updateCoupleIdSetNull(coupleId);
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmailValue(email)) {
            throw new DuplicateEmailException(email, ErrorCode.DUPLICATE_EMAIL);
        }
    }
}
