package com.ohdaesan.shallwepets.auth.service;

import com.ohdaesan.shallwepets.global.UserStatusException;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.domain.entity.Status;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Autowired
    public CustomUserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new AuthenticationServiceException(memberId + "님은 가입되지 않았습니다."));

        if (member.getStatus() != Status.ACTIVATED) {
            throw new UserStatusException("회원 status가 활성화 되어있지 않습니다.");
        }

        return new CustomUserDetails(member);
    }
}
