package com.ohdaesan.shallwepets.member.service;

import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Grade;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDTO register(MemberDTO memberDTO) {
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

    public String findMemberIdByMemberNameAndMemberEmail(String name, String email) {
        Member member = memberRepository.findMemberByMemberNameAndMemberEmail(name, email)
                .orElseThrow(() -> new NoSuchElementException("해당 이름과 이메일로 가입한 회원이 존재하지 않습니다."));
        return member.getMemberId();
    }

    public String findMemberIdByMemberNameAndMemberPhone(String name, String phone) {
        Member member = memberRepository.findMemberByMemberNameAndMemberPhone(name, phone)
                .orElseThrow(() -> new NoSuchElementException("해당 이름과 번호로 가입한 회원이 존재하지 않습니다."));
        return member.getMemberId();
    }

    public String findNicknameByMemberNo(Long memberNo) {
        Member member = memberRepository.findMemberByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당 번호의 회원이 존재하지 않습니다."));
        return member.getMemberNickname();
    }

    public String findGradeByMemberNo(Long memberNo) {
        Member member = memberRepository.findMemberByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당 번호의 회원이 존재하지 않습니다."));
        return member.getGrade().toString();
    }

    public boolean existsByMemberIdAndMemberNameAndMemberEmail(String memberId, String name, String email) {
        return memberRepository.existsByMemberIdAndMemberNameAndMemberEmail(memberId, name, email);
    }

    public boolean existsByMemberIdAndMemberNameAndMemberPhone(String memberId, String name, String phone) {
        return memberRepository.existsByMemberIdAndMemberNameAndMemberPhone(memberId, name, phone);
    }

    public boolean isPasswordInUse(String memberId, String rawPassword) {
        Member member = memberRepository.findByMemberId(memberId);

        if (member == null) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        return passwordEncoder.matches(rawPassword, member.getMemberPwd());
    }

    public void updatePassword(String memberId, String modifiedPw) {
        log.info("비밀번호 업데이트 메서드 시작....");
        String encodedPassword = passwordEncoder.encode(modifiedPw);
        int updatedCount = memberRepository.updateMemberPwByMemberId(memberId, encodedPassword);

        // 업데이트가 실행 안되었을 때
        if (updatedCount == 0) {
            throw new NoSuchElementException("존재하지 않는 회원입니다.");
        }

    }

    public boolean existsMemberId(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public boolean existsNickname(String memberNickname) {
        return memberRepository.existsByMemberNickname(memberNickname);
    }

    public boolean existsEmail(String memberEmail) {
        return memberRepository.existsByMemberEmail(memberEmail);
    }

    public boolean existsPhone(String memberPhone) {
        return memberRepository.existsByMemberPhone(memberPhone);
    }

    // memberNo로 Member 조회 채팅 기능에서 사용하려고 생성(by 이득규)
    public Member findById(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다. memberNo:" + memberNo));
    }

    public Long findImageNoById (Long memberNo) {
        return memberRepository.findImageNoByMemberNo(memberNo);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll(); // Member 엔티티 리스트를 그대로 반환
    }
}
