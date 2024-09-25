package com.ohdaesan.shallwepets.mypage.controller;

import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.mypage.domain.dto.ChangePasswordDTO;
import com.ohdaesan.shallwepets.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "MyPage")
@RestController
@RequestMapping("/mypage")
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/my_info")
    public ResponseEntity<MemberDTO> getMyInfo(Authentication authentication) {
        String memberId = authentication.getName();
        MemberDTO memberDTO = myPageService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberDTO);
    }

    @PutMapping("/my_info")
    public ResponseEntity<MemberDTO> updateMyInfo(Authentication authentication, @RequestBody MemberDTO updatedMemberDTO) {
        String memberId = authentication.getName();
        MemberDTO updatedInfo = myPageService.updateMemberInfo(memberId, updatedMemberDTO);
        return ResponseEntity.ok(updatedInfo);
    }

    @PostMapping("/profile_picture")
    public ResponseEntity<String> uploadProfilePicture(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        String memberId = authentication.getName();
        String fileUrl = myPageService.uploadProfilePicture(memberId, file);
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping("/check_id")
    public ResponseEntity<String> checkIdDuplication(@RequestParam String memberId) {
        boolean isDuplicate = myPageService.checkIdDuplication(memberId);
        String message = isDuplicate ? "이미 등록된 아이디입니다." : "사용할 수 있는 아이디입니다.";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/check_nickname")
    public ResponseEntity<String> checkNicknameDuplication(@RequestParam String nickname) {
        boolean isDuplicate = myPageService.checkNicknameDuplication(nickname);
        String message = isDuplicate ? "이미 등록된 닉네임입니다." : "사용할 수 있는 닉네임입니다.";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/check_email")
    public ResponseEntity<String> checkEmailDuplication(@RequestParam String email) {
        boolean isDuplicate = myPageService.checkEmailDuplication(email);
        String message = isDuplicate ? "이미 등록된 이메일입니다." : "사용할 수 있는 이메일입니다.";
        return ResponseEntity.ok(message);
    }

    @GetMapping("/check_phone")
    public ResponseEntity<String> checkPhoneDuplication(@RequestParam String phone) {
        boolean isDuplicate = myPageService.checkPhoneDuplication(phone);
        String message = isDuplicate ? "이미 등록된 전화번호입니다." : "사용할 수 있는 전화번호입니다.";
        return ResponseEntity.ok(message);
    }

    @PostMapping("/change_password")
    public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody ChangePasswordDTO changePasswordDTO) {
        String memberId = authentication.getName();
        myPageService.changePassword(memberId, changePasswordDTO);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


}
