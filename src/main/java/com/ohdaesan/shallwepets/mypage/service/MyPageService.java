package com.ohdaesan.shallwepets.mypage.service;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.mypage.domain.dto.ChangePasswordDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "gif");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public MemberDTO getMemberInfo(String memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }
        return modelMapper.map(member, MemberDTO.class);
    }

    @Transactional
    public MemberDTO updateMemberInfo(String memberId, MemberDTO updatedMemberDTO) {
        Member existingMember = memberRepository.findByMemberId(memberId);
        if (existingMember == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 아이디 길이 검증
        if (updatedMemberDTO.getMemberId().length() >= 20) {
            throw new IllegalArgumentException("아이디는 20자 미만이어야 합니다.");
        }

        // 업데이트할 필드만 변경
        Member updatedMember = Member.builder()
                .memberId(existingMember.getMemberId())
                .memberPwd(existingMember.getMemberPwd())
                .memberNickname(updatedMemberDTO.getMemberNickname())
                .memberName(updatedMemberDTO.getMemberName())
                .memberEmail(updatedMemberDTO.getMemberEmail())
                .memberPhone(updatedMemberDTO.getMemberPhone())
                .memberDob(updatedMemberDTO.getMemberDob())
                .memberAddress(updatedMemberDTO.getMemberAddress())
                .image(existingMember.getImage()) // 기존 이미지를 그대로 사용
                .build();

        Member savedMember = memberRepository.save(updatedMember);
        return modelMapper.map(savedMember, MemberDTO.class);
    }

    @Transactional
    public String uploadProfilePicture(String memberId, MultipartFile file) throws IOException {
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 파일 확장자 검증
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

        // 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 파일 저장 로직 (로컬 파일 시스템에 저장)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();  // 파일명에 현재 시간을 추가하여 유니크한 이름 생성
        String filePath = "path/to/save/" + fileName;  // 실제 저장 경로로 변경해야 합니다.

        // 파일 저장 (예: 파일 시스템에 저장)
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        java.nio.file.Files.write(path, file.getBytes());

        // 기존 이미지 엔티티가 있다면 가져오고, 없으면 새로 생성
        Images image = member.getImage();
        if (image == null) {
            image = Images.builder()
                    .imageName(file.getOriginalFilename())       // 원본 파일명
                    .imageSavedName(fileName)                    // 저장된 파일명
                    .imageSavedPath(filePath)                    // 저장된 파일 경로
                    .imageUrl("/images/" + fileName)             // URL 경로 (이 부분은 나중에 실제 서비스에 맞게 변경)
                    .build();
        } else {
            // 기존 이미지 엔티티가 있을 경우 업데이트
            image = Images.builder()
                    .imageNo(image.getImageNo())
                    .imageName(file.getOriginalFilename())
                    .imageSavedName(fileName)
                    .imageSavedPath(filePath)
                    .imageUrl("/images/" + fileName)  // URL 경로는 실제 서비스에 맞게 수정
                    .build();
        }

        // Member 객체에 이미지 엔티티 설정
        Member updatedMember = Member.builder()
                .memberId(member.getMemberId())
                .memberPwd(member.getMemberPwd())
                .memberNickname(member.getMemberNickname())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberDob(member.getMemberDob())
                .memberAddress(member.getMemberAddress())
                .image(image)  // 이미지 엔티티 설정
                .build();

        memberRepository.save(updatedMember);

        return filePath;
    }

    public boolean checkIdDuplication(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return memberRepository.existsByMemberNickname(nickname);
    }

    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    public boolean checkPhoneDuplication(String phone) {
        return memberRepository.existsByMemberPhone(phone);
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Transactional
    public void changePassword(String memberId, ChangePasswordDTO changePasswordDTO) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!member.getMemberPwd().equals(changePasswordDTO.getCurrentPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 검증
        String newPassword = changePasswordDTO.getNewPassword();
        if (newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("새 비밀번호는 8자 이상 20자 미만이어야 합니다.");
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!newPassword.equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 변경
        Member updatedMember = Member.builder()
                .memberId(member.getMemberId())
                .memberPwd(newPassword) // 새로운 비밀번호 설정
                .memberNickname(member.getMemberNickname())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberDob(member.getMemberDob())
                .memberAddress(member.getMemberAddress())
                .image(member.getImage())
                .build();

        memberRepository.save(updatedMember);
    }
}

