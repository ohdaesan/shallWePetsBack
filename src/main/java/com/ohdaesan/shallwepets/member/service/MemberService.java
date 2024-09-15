package com.ohdaesan.shallwepets.member.service;

import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDTO register(MemberDTO memberDTO) {
        // 중복체크 (optional)
        // email을 사용자 특정하는데 사용 => 이메일은 중복 X

        // 이메일 중복 체크
        // DB에서 email로 Member를 조회했을 때 존재하면 Exception 처리

        // 비밀번호 암호화
        memberDTO.setMemberPwd(passwordEncoder.encode(memberDTO.getMemberPwd()));
        memberDTO.setMemberRole(/*RoleType.USER*/ "USER");

        // 데이터베이스에 저장하기 위해 DTO에 담긴 값을 Entity로 변경
        Member registerMember = modelMapper.map(memberDTO, Member.class);

        // 저장
        Member savedMember = memberRepository.save(registerMember);

        MemberDTO responseMemberDTO = modelMapper.map(savedMember, MemberDTO.class);

        return responseMemberDTO;
    }
}
